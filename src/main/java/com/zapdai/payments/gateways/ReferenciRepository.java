package com.zapdai.payments.gateways;

import com.zapdai.payments.domain.vo.referecieDTO.ReferencieDTO;
import com.zapdai.payments.domain.vo.referecieDTO.ReferencieResponseDTO;

public interface ReferenciRepository {
    ReferencieResponseDTO salveReferencie(ReferencieDTO referencieDTO);
}
