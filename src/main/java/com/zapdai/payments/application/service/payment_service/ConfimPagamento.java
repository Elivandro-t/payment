package com.zapdai.payments.application.service.payment_service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ConfimPagamento {
    public void ConfirmPagamento(Long idPagamento) throws MPException, MPApiException {
        MercadoPagoConfig.setAccessToken("APP_USR-162054304887036-050718-6176f4dbe6d6eec4415e2e8ae449190a-614057233");
        PaymentClient paymentClient = new PaymentClient();
        Payment payment = paymentClient.get(idPagamento);
        String Email = payment.getPayer().getEmail();
        String name = payment.getPayer().getFirstName();
        BigDecimal valorPago = payment.getTransactionAmount();
        List<PaymentItem> items = payment.getAdditionalInfo().getItems();
        String status = payment.getStatus();
        java.time.OffsetDateTime dateCreated = payment.getDateCreated();
        String referencia = payment.getExternalReference();

        if(items!=null){
           for (PaymentItem item : items) {
               System.out.println("Título: " + item.getTitle());
               System.out.println("Descrição: " + item.getDescription());
               System.out.println("Quantidade: " + item.getQuantity());
               System.out.println("Preço unitário: " + item.getUnitPrice());


           }
       }
       String dada = """
               detahes de pagamento
               usuario: %s
               status: %s
               data: %s
               email: %s
               valorPgado: %.2f
               referencia: %s
               """.formatted(name,status,dateCreated,Email,valorPago,referencia);
       System.out.println(dada);

    }

}
