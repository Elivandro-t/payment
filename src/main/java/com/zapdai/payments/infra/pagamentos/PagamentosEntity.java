package com.zapdai.payments.infra.pagamentos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zapdai.payments.domain.Pagamentos;
import com.zapdai.payments.domain.vo.ItensDoCarrinho;
import com.zapdai.payments.domain.vo.PagamentoDTO;
import com.zapdai.payments.domain.vo.PagamentoResponseDTO;
import com.zapdai.payments.domain.vo.PayerDTO;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

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
    private String dateCreated;
    private String planoId;
    private String planoName;
    private String PagamentoRef;
    private String userName;
    private BigDecimal valorPago;
    private String email;
    private LocalDateTime created;
    private String formaDePagamento;
    private String consumerId;
    private String cardId;
    private String paymenteId;
    private String customerID;

//    @Version
//    private Long version;

    public PagamentosEntity(Pagamentos pg) {
        this.dateCreated = pg.getDateCreated();
        this.email = pg.getEmail();
        this.userName = pg.getUserName();
        PagamentoRef = pg.getPagamentoRef();
        this.planoName = pg.getPlanoName();
        this.planoId = pg.getPlanoId();
        this.statusPago = pg.isStatusPago();
        this.statusPagoPlano = pg.getStatusPagoPlano();
        this.status = pg.getStatus();
        this.valorPago = pg.getValorPago();
        this.created = LocalDateTime.now();
        this.formaDePagamento = pg.getFormaDePagamento();
        this.consumerId = pg.getConsumerId();
        this.cardId = pg.getCardId();
        this.planoId = pg.getPlanoId();
        this.consumerId = pg.getConsumerId();
    }
    public PagamentosEntity(){}
    public  void pegaConsumer(String consumerId,String carrId,String payentID){
        this.consumerId = consumerId;
        this.cardId = carrId;
        this.paymenteId = payentID;

    }


    public void AtualizaPgamento(String data,
                                 StatusPagamento status,
                                 HistoryPagamento novoStatus, boolean ativo, PagamentoDTO p
                                 ) {
        this.dateCreated = data;
        this.statusPago = ativo;
        this.statusPagoPlano = status;
        novoStatus.setPagamento(this);
        this.status.add(novoStatus);
        if (!Objects.equals(this.pagamentoId,p.planoId())){
            this.planoId = p.planoId();
            this.planoName = p.planoName();
            this.created = LocalDateTime.now();
        }
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
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


    public String getConsumerId() {
        return consumerId;
    }

    public String getPaymenteId() {
        return paymenteId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }


    public void setPaymenteId(String paymenteId) {
        this.paymenteId = paymenteId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public void setFormaDePagamento(String formaDePagamento) {
        this.formaDePagamento = formaDePagamento;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getCustomerID() {
        return customerID;
    }
}
