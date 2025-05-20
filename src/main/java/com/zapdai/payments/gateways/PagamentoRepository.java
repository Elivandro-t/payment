package com.zapdai.payments.gateways;

import com.zapdai.payments.domain.vo.BuscaEmail;
import com.zapdai.payments.domain.vo.PagamentoDTO;
import com.zapdai.payments.domain.vo.PagamentoResponseDTO;

import java.util.List;

public interface PagamentoRepository {
     PagamentoResponseDTO save(PagamentoDTO pagamentoDTO, String statusMercadoPago);
     List<PagamentoResponseDTO> FindAll();
     PagamentoResponseDTO findOne(BuscaEmail email);
}
