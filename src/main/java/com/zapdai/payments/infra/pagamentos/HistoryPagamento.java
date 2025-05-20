package com.zapdai.payments.infra.pagamentos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

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

    public HistoryPagamento(PagamentosEntity pagamento, StatusPagamento status) {
        this.pagamento = pagamento;
        this.status = status;
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
}
