package com.zapdai.payments.web.paymentControlerMercadoPago.PagamentosControler;

import com.zapdai.payments.infra.PaymentAsaas;
import com.zapdai.payments.infra.PaymentRequest;
import com.zapdai.payments.infra.websocketconf.PaymentInteface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("v1/payment/zapdai")
public class AsaasControlerPaiment
{
    @Autowired
    PaymentAsaas paymentAsaas;
    @PostMapping
     public ResponseEntity<PaymentInteface> payment(@RequestBody @Valid PaymentRequest paymentRequest) throws IOException, InterruptedException {
       var resposnde =  paymentAsaas.processPayment(paymentRequest);
       return ResponseEntity.ok().body(resposnde);
     }
}
