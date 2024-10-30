package com.ecom.store.payments.controllers;

import com.ecom.store.payments.service.PaymentService;
import com.ecom.store.payments.dtos.PaymentRequestDto;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/afterCompletion")
    public String afterCompletion() {
        return "Payment successful";
    }

    @PostMapping("/payments")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequestDto paymentRequest) throws StripeException {
        String paymentLink = paymentService.createLink(paymentRequest.getOrderId(), paymentRequest.getAmount());
        return ResponseEntity.ok(paymentLink);
    }
}
