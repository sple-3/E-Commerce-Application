package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDTO {
    private String cardNumber;
    private String expiryDate; // MM/YY
    private String cvv;
    private String cardHolderName;
}