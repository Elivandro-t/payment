package com.zapdai.payments.application.service.payment_service.mercadopago;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.zapdai.payments.domain.vo.CardPaymentDTO;
import com.zapdai.payments.domain.vo.ItensDoCarrinho;
import com.zapdai.payments.domain.vo.PaymentResponseDTO;
import com.zapdai.payments.gateways.PagamentoRepository;
import com.zapdai.payments.infra.pagamentos.PagamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Prox {
    @Autowired
    private PagamentoRepository repository;
    @Value("${mercado_pago_sample_access_token}")
    private String mercadoPagoAccessToken;
    @Autowired
    private Currentepaiment currentepaiment;
    @Autowired
    private PagamentosRepository repositoryPagamentos;
    public PaymentResponseDTO processPayment(CardPaymentDTO request, String token) {
        var existePagamento = repositoryPagamentos.findOneByEmailCluster(request.getPayer().getEmail());
        if(existePagamento!=null){
            for (ItensDoCarrinho item:request.getItens()){
                if(existePagamento.getPlanoId().equals(item.getId())){
                    throw new RuntimeException("Plano ja incluido");

                }
            }
        }

        return valida(request, token);

    }
    private PaymentResponseDTO valida(CardPaymentDTO request, String token){
        try {
            MercadoPagoConfig.setAccessToken(token);
            String uniqueValue = System.currentTimeMillis() + "-" + UUID.randomUUID().toString();
            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("x-idempotency-key", uniqueValue);
            MPRequestOptions requestOptions = MPRequestOptions.builder()
                    .customHeaders(customHeaders)
                    .build();
            if(request.getItens()==null){
                throw new RuntimeException("Informar itens da lista!");
            }
            PaymentClient paymentClient = new PaymentClient();
            PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                    .transactionAmount(request.getTransactionAmount())
                    .issuerId(request.getIssuerId())// Valor da transação
                    .token(request.getToken()) // Token do cartão ou PIX
                    .description(request.getProductDescription()) // Descrição do produto
                    .installments(request.getInstallments()) // Parcelas
                    .paymentMethodId(request.getPaymentMethodId())
                    .statementDescriptor("zapdai")
                    .additionalInfo(paymentAdditionalInfoRequest(request))
                    .notificationUrl("https://server.zapdai.com/process_payment/webhook/mercadopago")
                    .externalReference(request.getPayer().getEmail())
                    .payer(PaymentPayerRequest.builder()
                            .email(request.getPayer().getEmail()) // Email do pagador
                            .firstName(request.getPayer().getFirst_name()) // Nome do pagador
                            .lastName(request.getPayer().getLast_name())
                            .identification(
                                    IdentificationRequest.builder()
                                            .type(request.getPayer().getIdentification().getType()) // Tipo de identificação (CPF, CNPJ)
                                            .number(request.getPayer().getIdentification().getNumber()) // Número de identificação
                                            .build()
                            )
                            .build())
                    .build();

            Payment createdPayment = paymentClient.create(paymentCreateRequest, requestOptions);
            String qrCodeBase = null;
            String qrCodeLink = null;

            if ("pix".equalsIgnoreCase(request.getPaymentMethodId())) {
                if (createdPayment.getPointOfInteraction() != null &&
                        createdPayment.getPointOfInteraction().getTransactionData() != null) {
                    qrCodeBase = createdPayment.getPointOfInteraction().getTransactionData().getQrCodeBase64();
                    qrCodeLink = createdPayment.getPointOfInteraction().getTransactionData().getQrCode(); // código Pix copia e cola
                } else {
                    throw new RuntimeException("QR Code não retornado.");
                }
            }

            var resposnde = new PaymentResponseDTO(
                    createdPayment.getId(),
                    String.valueOf(createdPayment.getStatus()),
                    createdPayment.getStatusDetail(),
                    createdPayment.getDateApproved(),
                    createdPayment.getPaymentMethodId(),
                    createdPayment.getPaymentTypeId(),
                    qrCodeBase,
                    qrCodeLink
            );

            return resposnde;
        } catch (MPApiException apiException) {
            System.out.println(apiException.getApiResponse().getContent());
            throw new RuntimeException(apiException.getApiResponse().getContent());
        } catch (MPException exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
    private PaymentAdditionalInfoRequest paymentAdditionalInfoRequest(CardPaymentDTO card){
        PaymentAdditionalInfoPayerRequest payer = PaymentAdditionalInfoPayerRequest.builder()
                .firstName(card.getPayer().getFirst_name())
                .lastName(card.getPayer().getLast_name())
                .build();
        List<PaymentItemRequest> items = card.getItens().stream()
                .map(item -> PaymentItemRequest.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getPrice())
                        .build())
                .toList();

        return PaymentAdditionalInfoRequest.builder()
                .items(items)
                .payer(payer)
                .build();
    }
}
