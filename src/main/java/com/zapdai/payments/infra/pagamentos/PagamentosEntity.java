package com.zapdai.payments.infra.pagamentos;

import jakarta.persistence.*;

@Entity
@Table(name = "pagamentos")
public class PagamentosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pagamentoId;
}
