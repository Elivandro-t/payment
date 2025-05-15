package com.zapdai.payments.application.service.payment_service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class DetalhesPagamentos {
    public void Gerar(String token){
        try {
            // Configure seu token de acesso
            MercadoPagoConfig.setAccessToken(token);

            // Crie o cliente de preferência
            PreferenceClient client = new PreferenceClient();

            // Crie um objeto Preference
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(getItems()) // Lista de itens
                    .payer(getPayer()) // Informações do pagador
                    .build();

            // Salve a preferência
            Preference preference = client.create(preferenceRequest);
            System.out.println("Link de pagamento: " + preference.getSandboxInitPoint());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static List<PreferenceItemRequest> getItems() {
        List<PreferenceItemRequest> items = new ArrayList<>();
        PreferenceItemRequest item = PreferenceItemRequest.builder()
                .id("ITEM_001") // Código do item (obrigatório)
                .title("Produto danone")
                .description("Descrição do produto exemplo") // Descrição (obrigatória)
                .quantity(1)
                .unitPrice(new BigDecimal("100.00"))
                .build();
        items.add(item);
        return items;
    }

    // Método para criar o pagador
    private static PreferencePayerRequest getPayer() {
        return PreferencePayerRequest.builder()
                .name("João") // Nome do pagador (obrigatório)
                .surname("Silva") // Sobrenome do pagador (obrigatório)
                .email("joao.silva@example.com")
                .build();
    }
}
