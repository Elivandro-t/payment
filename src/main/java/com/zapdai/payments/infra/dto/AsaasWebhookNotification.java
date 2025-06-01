package com.zapdai.payments.infra.dto;

public class AsaasWebhookNotification {
    public String event;
    private String dateCreated;
    private PaymentData payment;

    public AsaasWebhookNotification(String event,String dateCreated, PaymentData payment) {
        this.event = event;
        this.dateCreated = dateCreated;
        this.payment = payment;
    }

    public String getEvent() {
        return event;
    }


    public PaymentData getPayment() {
        return payment;
    }

    public String getDateCreated() {
        return dateCreated;
    }
}
