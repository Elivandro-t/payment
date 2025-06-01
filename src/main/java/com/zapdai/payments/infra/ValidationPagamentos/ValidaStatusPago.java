package com.zapdai.payments.infra.ValidationPagamentos;

import com.zapdai.payments.domain.Pagamentos;
import com.zapdai.payments.infra.pagamentos.PagamentosEntity;
import com.zapdai.payments.infra.pagamentos.StatusPagamento;

public class ValidaStatusPago {

    public void valid(Pagamentos pagamentos, PagamentosEntity entity){
        if(entity.getStatusPagoPlano().equals(StatusPagamento.PAGO)){

        }
    }
}
