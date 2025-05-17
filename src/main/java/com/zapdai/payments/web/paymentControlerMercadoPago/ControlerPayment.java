package com.zapdai.payments.web.paymentControlerMercadoPago;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.zapdai.payments.application.service.payment_service.ConfimPagamento;
import com.zapdai.payments.application.service.payment_service.DetalhesPagamentos;
import com.zapdai.payments.application.service.payment_service.PaymentService;
import com.zapdai.payments.domain.vo.CardPaymentDTO;
import com.zapdai.payments.domain.vo.PaymentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("process_payment")
public class ControlerPayment {

    @Autowired
    private PaymentService cardPaymentService;
    @Autowired
    private DetalhesPagamentos form;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ConfimPagamento confimPagamento;

    @PostMapping("/v1/access_token")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody @Valid CardPaymentDTO cardPaymentDTO, @RequestParam String token) {
        PaymentResponseDTO payment = cardPaymentService.processPayment(cardPaymentDTO,token);
        rabbitTemplate.convertAndSend("pagamento.ex","",payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }
    @GetMapping("/v2/access_token")
    public ResponseEntity<String> processPayment2(@RequestParam String token) {
        form.Gerar(token);
        return ResponseEntity.ok("enviado");
    }

    @PostMapping("/webhook/mercadopago")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        System.out.println("Notificação recebida: " + payload);
        return ResponseEntity.ok("Webhook recebido com sucesso!");
    }
    @GetMapping("/consulta/{id}")
    public ResponseEntity<String> detalhesPagamento(@PathVariable Long id) throws MPException, MPApiException {
        confimPagamento.ConfirmPagamento(id);
        return ResponseEntity.ok().body("Deuc certo");

    }
}
