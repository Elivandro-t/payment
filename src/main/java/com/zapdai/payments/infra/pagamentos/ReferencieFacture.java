package com.zapdai.payments.infra.pagamentos;

import com.zapdai.payments.domain.vo.referecieDTO.ReferencieDTO;
import com.zapdai.payments.domain.vo.referecieDTO.ReferencieResponseDTO;
import com.zapdai.payments.gateways.ReferenciRepository;
import com.zapdai.payments.infra.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReferencieFacture implements ReferenciRepository {
    @Autowired
    private ReferenciEntityRepository repository;
    @Override
    public ReferencieResponseDTO salveReferencie(ReferencieDTO referencieDTO) {
        var entity = repository.save(new Reference(referencieDTO));
        return new ReferencieResponseDTO(entity);
    }
}
