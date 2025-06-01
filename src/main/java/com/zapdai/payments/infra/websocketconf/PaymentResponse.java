package com.zapdai.payments.infra.websocketconf;

public class PaymentResponse implements PaymentInteface{
    public String customerId;
    public String subscriptionId;
    public String message;

    public PaymentResponse(String customerId, String subscriptionId, String message) {
        this.customerId = customerId;
        this.subscriptionId = subscriptionId;
        this.message = message;
    }


    @Override
    public String retornaInf() {
        return this.customerId;
    }

    @Override
    public String retornaqr() {
        return this.message;
    }

    @Override
    public String retornaInc() {
        return this.subscriptionId;
    }
}
