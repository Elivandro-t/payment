package com.zapdai.payments.application.service.payment_service.mercadopago;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.customer.CustomerCardClient;
import com.mercadopago.client.customer.CustomerCardCreateRequest;
import com.mercadopago.client.customer.CustomerClient;
import com.mercadopago.client.customer.CustomerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPSearchRequest;
import com.mercadopago.resources.customer.Customer;
import com.mercadopago.resources.customer.CustomerCard;
import com.zapdai.payments.application.service.payment_service.CustomerCardInfo;
import com.zapdai.payments.domain.vo.CardPaymentDTO;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

@Service
public class Currentepaiment {

    public CustomerCardInfo createCustomer(CardPaymentDTO request, String token)  {
       try {
           MercadoPagoConfig.setAccessToken(token);

           CustomerClient customerClient = new CustomerClient();
           MPSearchRequest searchRequest = MPSearchRequest.builder()
                   .offset(0)
                   .limit(1)
                   .filters(Map.of("email", request.getPayer().getEmail()))
                   .build();

           // Verificar se já existe um cliente com o mesmo e-mail
           List<Customer> customers = customerClient.search(searchRequest).getResults();

           Customer customer;
           if (!customers.isEmpty()) {
               customer = customers.get(0);
           } else {
               CustomerRequest customerRequest = CustomerRequest.builder()
                       .email(request.getPayer().getEmail())
                       .build();
               customer = customerClient.create(customerRequest);
           }

           // Criar cartão vinculado ao cliente
           CustomerCardClient customerCardClient = new CustomerCardClient();

           CustomerCardCreateRequest cardCreateRequest = CustomerCardCreateRequest.builder()
                   .token(request.getToken()) // token do cartão que veio do frontend
                   .paymentMethodId(request.getPaymentMethodId()) // Ex.: visa, master, elo, etc.
                   .build();

           CustomerCard customerCard = customerCardClient.create(customer.getId(), cardCreateRequest);

           return new CustomerCardInfo(customerCard.getCustomerId(), customerCard.getId());
       } catch (MPApiException | MPException e) {
           System.out.println("Status: " + e.getMessage());
           throw new RuntimeException(e);
       }
    }

    public String createPayment(BigDecimal amount, String customerId, String cardId, String paymentMethodId, String token) {
        try {
            URL url = new URL("https://api.mercadopago.com/v1/payments");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");

            // Gerando um idempotency key única para cada requisição
            String idempotencyKey = UUID.randomUUID().toString();
            conn.setRequestProperty("X-Idempotency-Key", idempotencyKey);

            conn.setDoOutput(true);

            String jsonBody = """
            {
                          "transaction_amount": %s,
                          "description": "plano zapdai",
                          "payment_method_id": "%s",
                          "payer": {
                            "type": "customer",
                            "id": "%s"
                          },
                          "installments": 1,
                          "statement_descriptor": "ZAPDAI",
                          "metadata": {},
                          "additional_info": {},
                          "issuer_id": null,
                          "capture": true
                        }
                    
        """.formatted(amount, paymentMethodId, customerId);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    System.out.println("Pagamento criado: " + responseBody);
                    return responseBody;
                }
            } else {
                try (Scanner scanner = new Scanner(conn.getErrorStream())) {
                    String errorResponse = scanner.useDelimiter("\\A").next();
                    throw new RuntimeException("Erro ao criar pagamento: " + errorResponse);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento: " + e.getMessage(), e);
        }
    }








}
