package com.zapdai.payments.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class CardPaymentDTO {
    private String token;

    private String issuerId;

    @NotBlank
    private String paymentMethodId;

    @NotNull
    private BigDecimal transactionAmount;

    private Integer installments;

    @NotBlank
    @JsonProperty("description")
    private String productDescription;

    private List<ItensDoCarrinho> itens;
    @NotNull
    private PayerDTO payer;
    private BackUrl backUrls;

    public CardPaymentDTO() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(String issuerId) {
        this.issuerId = issuerId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public PayerDTO getPayer() {
        return payer;
    }

    public void setPayer(PayerDTO payer) {
        this.payer = payer;
    }

    public List<ItensDoCarrinho> getItens() {
        return itens;
    }

    public void setItens(List<ItensDoCarrinho> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "CardPaymentDTO{" +
                "token='" + token + '\'' +
                ", issuerId='" + issuerId + '\'' +
                ", paymentMethodId='" + paymentMethodId + '\'' +
                ", transactionAmount=" + transactionAmount +
                ", installments=" + installments +
                ", productDescription='" + productDescription + '\'' +
                ", itens=" + itens +
                ", payer=" + payer +
                '}';
    }

    public BackUrl getBackUrls() {
        return backUrls;
    }

    public void setBackUrls(BackUrl backUrls) {
        this.backUrls = backUrls;
    }

}
