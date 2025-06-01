package com.zapdai.payments.infra.pagamentos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="history_pagamento")
public class HistoryPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    StatusPagamento status;
    @ManyToOne
    @JoinColumn(name = "pagamento_id")
    @JsonManagedReference
    private PagamentosEntity pagamento;
    private String planoID;
    private String planoName;
    LocalDateTime dataContratado;
    private String emailRef;
    private boolean ativo;

    public HistoryPagamento(PagamentosEntity pagamento, StatusPagamento status) {
        this.pagamento = pagamento;
        this.status = status;
        this.planoID = pagamento.getPlanoId();
        this.planoName = pagamento.getPlanoName();
        this.dataContratado = pagamento.getDataCreated();
//        this.ativo = pagamento.isStatusPago();
    }
    public HistoryPagamento(){}
    public void setId(long id) {
        this.id = id;
    }

    public void setPagamento(PagamentosEntity pagamento) {
        this.pagamento = pagamento;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public PagamentosEntity getPagamento() {
        return pagamento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public String getPlanoID() {
        return planoID;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public String getEmailRef() {
        return emailRef;
    }

    public LocalDateTime getDataContratado() {
        return dataContratado;
    }

    public String getPlanoName() {
        return planoName;
    }
}
