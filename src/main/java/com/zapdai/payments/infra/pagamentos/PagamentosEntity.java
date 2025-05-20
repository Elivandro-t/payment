package com.zapdai.payments.infra.pagamentos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zapdai.payments.domain.Pagamentos;
import com.zapdai.payments.domain.vo.ItensDoCarrinho;
import com.zapdai.payments.domain.vo.PayerDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "pagamentos")
public class PagamentosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pagamentoId;
    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<HistoryPagamento> status;
    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagoPlano;
    private boolean statusPago;
    private java.time.OffsetDateTime dateCreated;
    private String planoId;
    private String planoName;
    private long pagamentoIdMercadoPago;
    private String PagamentoRef;
    private String userName;
    private BigDecimal valorPago;
    private String email;
    private LocalDateTime created;
    private String formaDePagamento;
    @Version
    private Long version;

    public PagamentosEntity(Pagamentos pg) {
        this.dateCreated = pg.getDateCreated();
        this.email = pg.getEmail();
        this.userName = pg.getUserName();
        PagamentoRef = pg.getPagamentoRef();
        this.pagamentoIdMercadoPago = pg.getPagamentoIdMercadoPago();
        this.planoName = pg.getPlanoName();
        this.planoId = pg.getPlanoId();
        this.statusPago = pg.isStatusPago();
        this.statusPagoPlano = pg.getStatusPagoPlano();
        this.status = pg.getStatus();
        this.valorPago = pg.getValorPago();
        this.created = LocalDateTime.now();
        this.formaDePagamento = pg.getFormaDePagamento();
    }
    public PagamentosEntity(){}

    public void AtualizaPgamento(java.time.OffsetDateTime data,
                                 StatusPagamento status,
                                 HistoryPagamento novoStatus,boolean ativo
    ) {
        this.dateCreated = data;
        this.statusPago = ativo;
        this.statusPagoPlano = status;
        novoStatus.setPagamento(this);
        this.status.add(novoStatus);
        this.created = LocalDateTime.now();
    }
    public long getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(long pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public LocalDateTime getDataCreated() {
        return created;
    }

    public void setDataCreated(LocalDateTime dataCreated) {
        this.created = dataCreated;
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

    public void setStatusPagoPlano(StatusPagamento statusPagoPlano) {
        this.statusPagoPlano = statusPagoPlano;
    }

    public List<HistoryPagamento> getStatus() {
        return status;
    }

    public void setStatus(List<HistoryPagamento> status) {
        this.status = status;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getFormaDePagamento() {
        return formaDePagamento;
    }

    public Long getVersion() {
        return version;
    }
}
