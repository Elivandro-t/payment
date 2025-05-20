package com.zapdai.payments.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class PayerDTO {
    private long id;
    @NotBlank
    private String email;
    private String first_name;
    private String last_name;
    private String type;

    @NotNull
    private PayerIdentificationDTO identification;

    public PayerDTO() {
    }


    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PayerDTO{" +
                "email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", type='" + type + '\'' +
                ", identification=" + identification +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PayerIdentificationDTO getIdentification() {
        return identification;
    }

    public void setIdentification(PayerIdentificationDTO identification) {
        this.identification = identification;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getType() {
        return type;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
