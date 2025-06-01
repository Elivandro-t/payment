package com.zapdai.payments.application.service.payment_service;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.zapdai.payments.application.service.emailService.EmailService;
import com.zapdai.payments.domain.vo.PagamentoDTO;
import com.zapdai.payments.infra.dto.AsaasWebhookNotification;
import com.zapdai.payments.infra.pagamentos.ReferenciEntityRepository;
import com.zapdai.payments.infra.pagamentos.StatusPagamento;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PagamentoAsass {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PagamentoServiceZapdai pagamentoServiceZapdai;
    @Autowired
    ReferenciEntityRepository repository;


    @Async("taskExecutor")
    public void ConfirmPagamentos(AsaasWebhookNotification pagamento) throws MPException, MPApiException {
           var ref = repository.findById(pagamento.getPayment().getExternalReference());
           if(ref.isPresent()) {
               String email = ref.get().getEmail();
               String name = ref.get().getName();
               BigDecimal valorPago = BigDecimal.valueOf(ref.get().getValor());
               String status = pagamento.getPayment().getStatus();
               String formDePagamento = pagamento.getPayment().getBillingType();
               String referencia = pagamento.getPayment().getExternalReference();
               StatusPagamento statusP = null;
               boolean ativo = false;
               switch (status){
                   case "RECEIVED":
                       statusP = StatusPagamento.PAGO;
                       ativo = true;
                       break;
                   case "PENDING":
                       statusP = StatusPagamento.AGUARDANDO_APROVAÇÃO;
                       ativo = false;
                       break;
                   case "CANCELED":
                       statusP = StatusPagamento.CANCELADO;
                       ativo = false;
                       break;
                   default:
                       ativo = false;
                       statusP = StatusPagamento.PROCESSANDO_PAGAMENTO;
                       break;

               }
               System.out.println(statusP);

               var paga = new PagamentoDTO(statusP, ativo, pagamento.getDateCreated(), ref.get().getPlanoId(), ref.get().getNamePlano(), referencia, name, email, valorPago, formDePagamento, pagamento.getPayment().getCustomer());
               var resposta = pagamentoServiceZapdai.save(paga, status);
//        service.emailSend(email,valorPago,"Confimacao de pagamento");
               rabbitTemplate.convertAndSend("pagamento.ex","", resposta);
           }else {
               throw new RuntimeException("Sem pagamento encontrado");
           }
    }
}
