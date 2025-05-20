package com.zapdai.payments.domain;

import com.zapdai.payments.infra.pagamentos.PagamentosEntity;
import com.zapdai.payments.infra.pagamentos.StatusPagamento;
import jakarta.persistence.*;

public class HistoryPg {
    private long id;
    StatusPagamento status;
    private PagamentosEntity pagamento;
}
