package com.zapdai.payments.infra.pagamentos;

public enum StatusPagamento {

    APROVADO,
    RECUSADO,
    AGUARDANDO_APROVAÇÃO,
    CANCELADO,
    PROCESSANDO_PAGAMENTO,
    PAGO
}
