package com.zapdai.payments.web.paymentControlerMercadoPago;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.zapdai.payments.application.service.emailService.EmailService;
import com.zapdai.payments.application.service.payment_service.mercadopago.DetalhesPagamentos;
import com.zapdai.payments.application.service.payment_service.EbHookService;
import com.zapdai.payments.application.service.payment_service.mercadopago.PaymentService;
import com.zapdai.payments.domain.vo.CardPaymentDTO;
import com.zapdai.payments.domain.vo.PaymentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("process_payment")
public class ControlerPayment {

    @Autowired
    private PaymentService cardPaymentService;
    @Autowired
    private DetalhesPagamentos form;
//    @Autowired
//    private ConfimPagamento confimPagamento;
    @Autowired
    EbHookService service;
    @Autowired
    EmailService emailService;
//
//    @PostMapping("/v1/access_token")
//    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody @Valid CardPaymentDTO cardPaymentDTO, @RequestParam String token) throws MPException, MPApiException {
//      var v = cardPaymentService.processPayment(cardPaymentDTO,token);
//        return ResponseEntity.status(HttpStatus.CREATED).body(v);
//    }
//    @GetMapping("/v2/access_token")
//    public ResponseEntity<String> processPayment2(@RequestParam String token) {
//        form.Gerar(token);
//        return ResponseEntity.ok("enviado");
//    }

    @PostMapping("/webhook/asaas")
    public ResponseEntity<Map<String,String>> handleWebhook(@RequestBody String payload, UriComponentsBuilder uri) throws MPException, MPApiException {
            URI ur = uri.path("/v2/acess_token").build().toUri();
        service.processPayload(payload);
//        emailService.emailSend("elivandro78@gmail.com", BigDecimal.valueOf(1.2),payload);
        return ResponseEntity.created(ur).body(Map.of("msg","deu certo"));
    }
    @GetMapping("/consulta/{email}")
    public ResponseEntity<String> detalhesPagamento(@PathVariable String email) throws MPException, MPApiException {
        emailService.emailSend(email, BigDecimal.valueOf(100.0),"deu certo");
        return ResponseEntity.ok().body("Deuc certo");

    }
}
