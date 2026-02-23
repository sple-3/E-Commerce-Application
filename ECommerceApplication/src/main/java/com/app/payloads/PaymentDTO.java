package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

	private Long paymentId;
	private OrderDTO orderDTO;
	private String paymentMethod;
	private BankDTO bankDTO;
	private String promoCode;
	private Double adminFee;
	private Double paidAmount;

}
