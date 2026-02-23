package com.app.payloads;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

	private String promoCode;
	private double finalAmount;
	private double adminFee;
	private double totalPaidAmount;
	private String bankAccount;
}
