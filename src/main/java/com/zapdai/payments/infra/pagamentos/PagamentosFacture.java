package com.zapdai.payments.infra.pagamentos;

import com.zapdai.payments.domain.Pagamentos;
import com.zapdai.payments.domain.vo.BuscaEmail;
import com.zapdai.payments.domain.vo.PagamentoDTO;
import com.zapdai.payments.domain.vo.PagamentoResponseDTO;
import com.zapdai.payments.gateways.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PagamentosFacture implements PagamentoRepository {
    PagamentosRepository repository;
    public PagamentosFacture(PagamentosRepository pagamentosRepository){
        this.repository = pagamentosRepository;
    }
    @Override
    public PagamentoResponseDTO save(PagamentoDTO pagamentoDTO, String status) {
        List<HistoryPagamento> lista = new ArrayList<>();
        boolean ativo = false;
        PagamentosEntity resposta = repository.findOneByNameByConsumer(pagamentoDTO.consumerId());
        if(resposta!=null ){
            if(resposta.getStatusPagoPlano()==StatusPagamento.PAGO && resposta.getPlanoId().equals(pagamentoDTO.planoId())){
                return new PagamentoResponseDTO(resposta);
            }
            StatusPagamento novo;
            if(status.equals("RECEIVED")){
                ativo = true;
                novo = StatusPagamento.PAGO;
            } else {
                ativo = false;
                novo = valid(status);
            }

            HistoryPagamento historyPagamento = new HistoryPagamento(resposta,novo);
            resposta.AtualizaPgamento(pagamentoDTO.dateCreated(),
                   novo,historyPagamento,ativo,pagamentoDTO);
            var pagamentoAtualizado = repository.save(resposta);
            return new PagamentoResponseDTO(pagamentoAtualizado);
        }
       else{
            Pagamentos pagamentos = new Pagamentos(pagamentoDTO,lista);
            PagamentosEntity pg = new PagamentosEntity(pagamentos); HistoryPagamento historyPagamento = new HistoryPagamento(pg,valid(status));
            lista.add(historyPagamento);
            var pagamento = repository.save(pg);
            return new PagamentoResponseDTO(pagamento);
        }
    }

    @Override
    public List<PagamentoResponseDTO> FindAll() {
      var lista =   repository.findAll().stream().map(e->new Pagamentos(e)).toList();
      return lista.stream().map(PagamentoResponseDTO::new).toList();
    }

    @Override
    public PagamentoResponseDTO findOne(BuscaEmail busca) {
        var response = repository.findOneByEmail(busca.email(),StatusPagamento.PAGO);
        if(response!=null){
            return new PagamentoResponseDTO(response);

        }
        throw new RuntimeException("Sem pagamentos Concluidos");
    }


    private StatusPagamento valid(String status){
        StatusPagamento statusP;
        switch (status){
            case "RECEIVED":
                statusP = StatusPagamento.PAGO;
                break;
            case "PENDING":
                statusP = StatusPagamento.AGUARDANDO_APROVAÇÃO;
                break;
            case "CANCELED":
                statusP = StatusPagamento.CANCELADO;
                break;
            case  "CONFIRMED":
                statusP = StatusPagamento.CONFIRMADO_NAO_PAGO;
            default:
                statusP = StatusPagamento.PROCESSANDO_PAGAMENTO;
        }
        return statusP;
    }
}
