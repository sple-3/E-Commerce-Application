package com.app.services;

import com.app.payloads.PaymentDTO;
import com.app.payloads.PaymentResponse;

public interface PaymentService {

    PaymentResponse checkout(PaymentDTO paymentDTO);

    PaymentDTO getPaymentById(Long paymentId);

}
