package com.zapdai.payments.infra.websocketconf;

public class PaymentResponsePix implements PaymentInteface {
    public String qr;
    public String chave;
    public String message;
    public String expiration;

    public PaymentResponsePix(String qr, String chave, String message,String exp) {
        this.qr = qr;
        this.chave = chave;
        this.message = message;
        this.expiration=exp;
    }

    @Override
    public String retornaInf() {
        return qr;
    }

    @Override
    public String retornaqr() {
        return chave;
    }

    @Override
    public String retornaInc() {
        return message;
    }

    public String getExpiration() {
        return expiration;
    }
}
