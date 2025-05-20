package com.zapdai.payments.application.service.payment_service;
import com.zapdai.payments.domain.vo.BuscaEmail;
import com.zapdai.payments.domain.vo.PagamentoDTO;
import com.zapdai.payments.domain.vo.PagamentoResponseDTO;
import com.zapdai.payments.gateways.PagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PagamentoServiceZapdai {
    private  PagamentoRepository repository;
    public PagamentoServiceZapdai(PagamentoRepository repository){
        this.repository = repository;
    }
   @Transactional
    public PagamentoResponseDTO save(PagamentoDTO p, String status){
        return repository.save(p,status);
    }

    public Map<String, List<PagamentoResponseDTO>> findAll(){
        Map<String,List<PagamentoResponseDTO>> listMap = new HashMap<>();
        var lista =  repository.FindAll();
        listMap.put("pagamentos",lista);
        return listMap;
    }
    public Map<String, PagamentoResponseDTO> findOne(BuscaEmail busca){
        Map<String,PagamentoResponseDTO> listMap = new HashMap<>();
        var lista =  repository.findOne(busca);
        listMap.put("pagamento",lista);
        return listMap;
    }

}
