package com.zapdai.payments.application.service.payment_service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentItem;
import com.zapdai.payments.application.service.emailService.EmailService;
import com.zapdai.payments.domain.Pagamentos;
import com.zapdai.payments.domain.vo.PagamentoDTO;
import com.zapdai.payments.infra.pagamentos.HistoryPagamento;
import com.zapdai.payments.infra.pagamentos.PagamentosEntity;
import com.zapdai.payments.infra.pagamentos.StatusPagamento;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConfimPagamento {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private EmailService service;
   private PagamentoServiceZapdai pagamentoServiceZapdai;
   public ConfimPagamento(EmailService emailService,PagamentoServiceZapdai pagamentoServiceZapdai){

       this.service = emailService;
       this.pagamentoServiceZapdai = pagamentoServiceZapdai;
    }
    @Async("taskExecutor")
    public void ConfirmPagamentos(Long idPagamento) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken("APP_USR-162054304887036-050718-6176f4dbe6d6eec4415e2e8ae449190a-614057233");
        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.get(idPagamento);
        String Email = null;
        String name =  payment.getAdditionalInfo().getPayer().getFirstName();
        String sobrenome =  payment.getAdditionalInfo().getPayer().getLastName();
        BigDecimal valorPago = payment.getTransactionAmount();
        List<PaymentItem> items = payment.getAdditionalInfo().getItems();
        String status = payment.getStatus();
        String formDePagamento = payment.getPaymentMethodId();
        java.time.OffsetDateTime dateCreated = payment.getDateCreated();
        String referencia = payment.getExternalReference();
        System.out.println("Payer raw JSON: " +  payment.getAdditionalInfo().getPayer().getFirstName());
        PaymentItem planoItens = null;
        if(items!=null){
           for (PaymentItem item : items) {
               planoItens = item;
               System.out.println("Título: " + item.getTitle());
               System.out.println("Descrição: " + item.getDescription());
               System.out.println("Quantidade: " + item.getQuantity());
               System.out.println("Preço unitário: " + item.getUnitPrice());


           }
       }
        if(payment.getPayer().getEmail()!=null){
            Email = payment.getPayer().getEmail();
        }else{
            Email = referencia;
        }
       String data = """
               detahes de pagamento
               usuario: %s 
               status: %s
               data: %s
               email: %s
               valorPgado: %.2f
               referencia: %s
               """.formatted(name+" "+sobrenome,status,dateCreated,Email,valorPago,referencia);
       System.out.println(data);

        List<HistoryPagamento> lista = new ArrayList<>();
        StatusPagamento statusP = null;
           boolean ativo = false;
            if(status.equals("approved")){
                statusP = StatusPagamento.PAGO;
                ativo = true;
            }else if(status.equals("pending")){
                statusP = StatusPagamento.AGUARDANDO_APROVAÇÃO;
                ativo = false;
            }else if(status.equals("cancelled")){
                statusP = StatusPagamento.CANCELADO;
                ativo = false;
            }else{
                ativo = false;
                statusP = StatusPagamento.PROCESSANDO_PAGAMENTO;
            }
       var paga = new PagamentoDTO(statusP,ativo,dateCreated,planoItens.getId(),planoItens.getTitle(),idPagamento,referencia,name,Email,valorPago,formDePagamento);
      var resposta =  pagamentoServiceZapdai.save(paga,status);
       service.emailSend(Email,valorPago,"Confimacao de pagamento");
       rabbitTemplate.convertAndSend("pagamento.concluido",resposta);
    }
}
