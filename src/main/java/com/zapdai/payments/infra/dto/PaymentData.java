package com.zapdai.payments.infra.dto;

public class PaymentData {
    private String id;
    private String customer;
    private String status;
    private Double value;
    private String externalReference;
    private String paymentDate;
    private String pixQrCodeId;
    private String pixTransaction;
    private String description;
    private  String billingType;

    public String getId() {
        return id;
    }

    public Double getValue() {
        return value;
    }

    public String getStatus() {
        return status;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public String getPixTransaction() {
        return pixTransaction;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getPixQrCodeId() {
        return pixQrCodeId;
    }

    public String getDescription() {
        return description;
    }

    public String getBillingType() {
        return billingType;
    }

    public String getCustomer() {
        return customer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public void setPixQrCodeId(String pixQrCodeId) {
        this.pixQrCodeId = pixQrCodeId;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPixTransaction(String pixTransaction) {
        this.pixTransaction = pixTransaction;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }
}
