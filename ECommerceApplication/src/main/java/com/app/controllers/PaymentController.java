package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.payloads.PaymentDTO;
import com.app.payloads.PaymentResponse;
import com.app.services.PaymentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Checkout Payment
     * POST /api/public/users/{email}/orders/{orderId}/checkout
     */
    @PostMapping("/public/users/{email}/orders/{orderId}/checkout")
    public ResponseEntity<PaymentResponse> checkout(
            @PathVariable String email,
            @PathVariable Long orderId,
            @RequestBody PaymentDTO paymentDTO) {

        // pastikan orderId dari path diset ke DTO
        paymentDTO.getOrderDTO().setOrderId(orderId);

        PaymentResponse response = paymentService.checkout(paymentDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get Payment By Id (Admin)
     * GET /api/admin/payments/{paymentId}
     */
    @GetMapping("/admin/payments/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentById(
            @PathVariable Long paymentId) {

        PaymentDTO payment = paymentService.getPaymentById(paymentId);

        return new ResponseEntity<>(payment, HttpStatus.FOUND);
    }

}
