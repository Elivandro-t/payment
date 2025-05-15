package com.zapdai.payments.domain.vo;

import jakarta.validation.constraints.NotBlank;

public class PayerIdentificationDTO {
    @NotBlank
    private String type;
    @NotBlank
    private String number;

    public PayerIdentificationDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
