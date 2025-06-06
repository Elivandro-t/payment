package com.zapdai.payments.domain.vo;

import com.zapdai.payments.infra.pagamentos.HistoryPagamento;
import com.zapdai.payments.infra.pagamentos.PagamentosEntity;
import com.zapdai.payments.infra.pagamentos.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

    public record PagamentoDTO(
                               StatusPagamento statusPagoPlano,
                               boolean statusPago,
                               String dateCreated, String planoId,
                               String planoName,
                               String PagamentoRef,
                               String userName,
                               String email,
                                BigDecimal valorPago,
                               String formaDePagamento,
                               String consumerId
    ) {
        public PagamentoDTO(PagamentosEntity pagamento) {
            this(
                    pagamento.getStatusPagoPlano(),
                    pagamento.isStatusPago(),
                    pagamento.getDateCreated(),
                    pagamento.getPlanoId(),
                    pagamento.getPlanoName(),
                    pagamento.getPagamentoRef(),
                    pagamento.getUserName(),
                    pagamento.getEmail(),
                    pagamento.getValorPago(),
                    pagamento.getFormaDePagamento(),
                    pagamento.getConsumerId()
            );
        }
    }
