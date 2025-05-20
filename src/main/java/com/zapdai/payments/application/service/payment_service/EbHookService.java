package com.zapdai.payments.application.service.payment_service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.zapdai.payments.domain.vo.MercadoPagoNotification;
import com.zapdai.payments.domain.vo.MercadoPagoNotificationV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EbHookService {
    @Autowired
  private  ConfimPagamento pagamentoService;
   private final Gson geson = new Gson();
    @Async("taskExecutor")
    public void processPayload(String payload){
        try {
            JsonObject json = JsonParser.parseString(payload).getAsJsonObject();
            String paymentId = null;
            if(json.has("data")){
                MercadoPagoNotification notification = geson.fromJson(payload,MercadoPagoNotification.class);
                paymentId = notification.getPaymentId();
            }
//            else if (json.has("resource")) {
//                // Tipo 2
//                MercadoPagoNotificationV2 noti2 = geson.fromJson(payload, MercadoPagoNotificationV2.class);
//                paymentId = noti2.getPaymentId();
//            }

            if (paymentId != null) {
                pagamentoService.ConfirmPagamentos(Long.valueOf(paymentId));
            } else {
                System.out.println("Não foi possível extrair o ID do pagamento.");
            }
        } catch (MPException | MPApiException e) {
            throw new RuntimeException(e);
        }
    }
}
