package com.zapdai.payments.application.service.payment_service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.zapdai.payments.domain.vo.MercadoPagoNotification;
import com.zapdai.payments.infra.dto.AsaasWebhookNotification;
import com.zapdai.payments.infra.dto.PaymentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EbHookService {
    @Value("${asaas.api.url}")
    private String apiUrl;

    @Value("${asaas.api.key}")
    private String apiKey;
    @Autowired
  private  PagamentoAsass pagamentoService;
   private final Gson geson = new Gson();
    @Async("taskExecutor")
    public void processPayload(String payload) throws MPException, MPApiException {
        AsaasWebhookNotification notification = geson.fromJson(payload, AsaasWebhookNotification.class);
        if ("PAYMENT_RECEIVED".equals(notification.event)) {
            PaymentData payment = notification.getPayment();
            System.out.println("Minhas datas "+payment.getStatus());
                pagamentoService.ConfirmPagamentos(notification);
        }

    }

    private void BuscaDetalhaesApi(String custumerId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl+"/customers/"+custumerId))
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("access_token",apiKey)
                .GET().build();
        HttpResponse response = client.send(request,HttpResponse.BodyHandlers.ofString());

        if(response.statusCode()>=200 && response.statusCode()< 300){
            System.out.println("Resposta "+response.body());
        }
    }
}
