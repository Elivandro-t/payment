package com.zapdai.payments.infra;

import com.zapdai.payments.domain.vo.ItensDoCarrinho;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PaymentRequest {
    public String paymentMethodId;
    public String name;
    public String cpfCnpj;
    public String email;
    public String phone;
    public String postalCode;
    public String addressNumber;
    public String addressComplement;
    public String creditCardNumber;
    public String creditCardCcv;
    public String creditCardExpiryMonth;
    public String creditCardExpiryYear;
    public Double value;
    @NotNull(message = "informa os itens do plano")
    public List<ItensDoCarrinho> itens;

}
