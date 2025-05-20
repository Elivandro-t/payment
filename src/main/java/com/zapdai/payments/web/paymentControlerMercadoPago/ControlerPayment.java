package com.zapdai.payments.web.paymentControlerMercadoPago;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.zapdai.payments.application.service.emailService.EmailService;
import com.zapdai.payments.application.service.payment_service.ConfimPagamento;
import com.zapdai.payments.application.service.payment_service.DetalhesPagamentos;
import com.zapdai.payments.application.service.payment_service.EbHookService;
import com.zapdai.payments.application.service.payment_service.PaymentService;
import com.zapdai.payments.domain.vo.CardPaymentDTO;
import com.zapdai.payments.domain.vo.PaymentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("process_payment")
public class ControlerPayment {

    @Autowired
    private PaymentService cardPaymentService;
    @Autowired
    private DetalhesPagamentos form;
    @Autowired
    private ConfimPagamento confimPagamento;
    @Autowired
    EbHookService service;
    @Autowired
    EmailService emailService;

    @PostMapping("/v1/access_token")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody @Valid CardPaymentDTO cardPaymentDTO, @RequestParam String token) {
        PaymentResponseDTO payment = cardPaymentService.processPayment(cardPaymentDTO,token);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }
    @GetMapping("/v2/access_token")
    public ResponseEntity<String> processPayment2(@RequestParam String token) {
        form.Gerar(token);
        return ResponseEntity.ok("enviado");
    }

    @PostMapping("/webhook/mercadopago")
    public void handleWebhook(@RequestBody String payload) {

        service.processPayload(payload);
    }
    @GetMapping("/consulta/{email}")
    public ResponseEntity<String> detalhesPagamento(@PathVariable String email) throws MPException, MPApiException {
        emailService.emailSend(email, BigDecimal.valueOf(100.0),"deu cetrto");
        return ResponseEntity.ok().body("Deuc certo");

    }
}
