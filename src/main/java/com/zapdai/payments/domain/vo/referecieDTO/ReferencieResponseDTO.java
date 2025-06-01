package com.zapdai.payments.domain.vo.referecieDTO;

import com.zapdai.payments.domain.vo.ItensDoCarrinho;
import com.zapdai.payments.infra.Reference;

import java.math.BigDecimal;
import java.util.List;

public record ReferencieResponseDTO(String id,String planoId,String planoName, String email, Double valor, String name) {
    public ReferencieResponseDTO(Reference entity) {
        this(entity.getId(),entity.getPlanoId(),entity.getNamePlano(),entity.getEmail(),entity.getValor(), entity.getName());
    }
}
