package com.zapdai.payments.web.paymentControlerMercadoPago.PagamentosControler;

import com.zapdai.payments.application.service.payment_service.PagamentoServiceZapdai;
import com.zapdai.payments.domain.vo.BuscaEmail;
import com.zapdai.payments.domain.vo.PagamentoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("pagamento/v1")
public class PagamentosControler {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    PagamentoServiceZapdai serviceZapdai;

  public   PagamentosControler(PagamentoServiceZapdai pagamentoServiceZapdai){
        this.serviceZapdai = pagamentoServiceZapdai;
    }

    @GetMapping("/findall")
    public ResponseEntity<Map<String, List<PagamentoResponseDTO>>> findall(){
     var lista = serviceZapdai.findAll();
     return ResponseEntity.ok().body(lista);
    }
    @PostMapping("/one")
    public ResponseEntity<Map<String, PagamentoResponseDTO>> findone(@RequestBody BuscaEmail busca){
        var lista = serviceZapdai.findOne(busca);
        return ResponseEntity.ok().body(lista);
    }
    @MessageMapping("/busca")
    public void listaMestricas(@Payload BuscaEmail busca){
        var lista = serviceZapdai.findOne(busca);
        messagingTemplate.convertAndSend("/topic/pagamentos/" + busca.email(), lista);

    };
}
