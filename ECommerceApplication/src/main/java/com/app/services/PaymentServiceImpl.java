package com.app.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.app.entites.*;
import com.app.payloads.*;
import com.app.repositories.*;
import com.app.services.PaymentService;

import jakarta.transaction.Transactional;

import com.app.exceptions.ResourceNotFoundException;

@Transactional
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepo orderRepo;
    private final PromoRepo promoRepo;
    private final BankRepo bankRepo;
    private final PaymentRepo paymentRepo;

    @Override
    public PaymentResponse checkout(PaymentDTO paymentDTO) {

        // Ambil Order
        Order order = orderRepo.findById(
                paymentDTO.getOrderDTO().getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order",
                                "orderId",
                                paymentDTO.getOrderDTO().getOrderId()
                        ));

        double originalAmount = order.getTotalAmount();
        double discount = 0.0;
        String appliedPromo = null;

        // Cek Promo jika ada
        if (paymentDTO.getPromoCode() != null &&
            !paymentDTO.getPromoCode().isEmpty()) {

            Promo promo = promoRepo
                    .findById(paymentDTO.getPromoCode())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Promo", 
                                    "promoCode",
                                    paymentDTO.getPromoCode()
                            ));

            discount = (originalAmount * promo.getDiscount()) / 100.0;
            appliedPromo = promo.getPromoCode();
        }

        // Hitung Final Amount
        double finalAmount = originalAmount - discount;

        // Ambil Bank dari DTO
        String bankName = paymentDTO.getBankDTO().getBankName();

        Bank bank = bankRepo.findByBankNameIgnoreCase(bankName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank",
                                "bankName",
                                bankName
                        ));

        // Simpan Payment (optional kalau mau persist)
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setBank(bank);

        paymentRepo.save(payment);

        // Return Response
        return new PaymentResponse(
                appliedPromo,
                finalAmount,
                bank.getAccountNumber()
        );
    }

    @Override
    public PaymentDTO getPaymentById(Long paymentId) {

        // Ambil Payment dari database
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "PaymentId", paymentId));

        // Convert ke DTO
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setPromoCode(payment.getPromoCode());

        // Map Order ke OrderDTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(payment.getOrder().getOrderId());
        orderDTO.setTotalAmount(payment.getOrder().getTotalAmount());
        paymentDTO.setOrderDTO(orderDTO);

        // Map Bank ke BankDTO
        BankDTO bankDTO = new BankDTO();
        bankDTO.setBankId(payment.getBank().getBankId());
        bankDTO.setBankName(payment.getBank().getBankName());
        bankDTO.setAccountNumber(payment.getBank().getAccountNumber());
        paymentDTO.setBankDTO(bankDTO);

        return paymentDTO;
}
}
