package com.zapdai.payments.application.service.payment_service;

import com.zapdai.payments.domain.vo.referecieDTO.ReferencieDTO;
import com.zapdai.payments.domain.vo.referecieDTO.ReferencieResponseDTO;
import com.zapdai.payments.infra.pagamentos.ReferencieFacture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferenceService {
    @Autowired
    private ReferencieFacture referencieFacture;

    public ReferencieResponseDTO save(ReferencieDTO referencieDTO){
        return referencieFacture.salveReferencie(referencieDTO);
    }
}
