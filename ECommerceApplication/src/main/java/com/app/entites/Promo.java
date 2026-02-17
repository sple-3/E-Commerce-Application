package com.app.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promo {
    
    @Id
    private String promoCode;

    @NotNull
    private double discount;
}
