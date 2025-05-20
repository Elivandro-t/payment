package com.zapdai.payments.domain;

import com.zapdai.payments.domain.vo.PagamentoDTO;
import com.zapdai.payments.infra.pagamentos.HistoryPagamento;
import com.zapdai.payments.infra.pagamentos.PagamentosEntity;
import com.zapdai.payments.infra.pagamentos.StatusPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public class Pagamentos {
    private long pagamentoId;
    private List<HistoryPagamento> status;
    private StatusPagamento statusPagoPlano;
    private boolean statusPago;
    private OffsetDateTime dateCreated;
    private String planoId;
    private String planoName;
    private long pagamentoIdMercadoPago;
    private String PagamentoRef;
    private String userName;
    private String email;
    private LocalDateTime dataCreated;
    private BigDecimal valorPago;
    private String formaDePagamento;


    public Pagamentos(PagamentoDTO pg,List<HistoryPagamento> p) {
        this.dataCreated = LocalDateTime.now();
        this.email = pg.email();
        this.userName = pg.userName();
        this.PagamentoRef = pg.PagamentoRef();
        this.pagamentoIdMercadoPago = pg.pagamentoIdMercadoPago();
        this.planoName = pg.planoName();
        this.planoId = pg.planoId();
        this.dateCreated = pg.dateCreated();
        this.statusPago = pg.statusPago();
        this.statusPagoPlano = pg.statusPagoPlano();
        this.status =p;
        this.valorPago = pg.valorPago();
        this.formaDePagamento = pg.formaDePagamento();
    }
    public Pagamentos(PagamentosEntity pg) {
        this.dataCreated = pg.getDataCreated();
        this.email = pg.getEmail();
        this.userName = pg.getUserName();
        this.PagamentoRef = pg.getPagamentoRef();
        this.pagamentoIdMercadoPago = pg.getPagamentoIdMercadoPago();
        this.planoName = pg.getPlanoName();
        this.planoId = pg.getPlanoId();
        this.dateCreated = pg.getDateCreated();
        this.statusPago = pg.isStatusPago();
        this.statusPagoPlano = pg.getStatusPagoPlano();
        this.status = pg.getStatus();
        this.valorPago = pg.getValorPago();
        this.formaDePagamento = pg.getFormaDePagamento();
    }
   public Pagamentos(){}

    public long getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(long pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public LocalDateTime getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(LocalDateTime dataCreated) {
        this.dataCreated = dataCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getPagamentoIdMercadoPago() {
        return pagamentoIdMercadoPago;
    }

    public void setPagamentoIdMercadoPago(long pagamentoIdMercadoPago) {
        this.pagamentoIdMercadoPago = pagamentoIdMercadoPago;
    }

    public String getPagamentoRef() {
        return PagamentoRef;
    }

    public void setPagamentoRef(String pagamentoRef) {
        PagamentoRef = pagamentoRef;
    }

    public String getPlanoName() {
        return planoName;
    }

    public void setPlanoName(String planoName) {
        this.planoName = planoName;
    }

    public String getPlanoId() {
        return planoId;
    }

    public void setPlanoId(String planoId) {
        this.planoId = planoId;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isStatusPago() {
        return statusPago;
    }

    public void setStatusPago(boolean statusPago) {
        this.statusPago = statusPago;
    }

    public StatusPagamento getStatusPagoPlano() {
        return statusPagoPlano;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public List<HistoryPagamento> getStatus() {
        return status;
    }

    public void setStatus(List<HistoryPagamento> status) {
        this.status = status;
    }

    public String getFormaDePagamento() {
        return formaDePagamento;
    }
}
