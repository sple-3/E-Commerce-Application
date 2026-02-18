package com.app.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "memberships")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    @NotBlank(message = "Kode membership tidak boleh kosong")
    @Column(unique = true, nullable = false)
    private String code;

    @Min(value = 0, message = "Diskon tidak boleh negatif")
    @Max(value = 100, message = "Diskon tidak boleh lebih dari 100%")
    @Column(nullable = false)
    private double discountPercent;

    @Column(nullable = false)
    private boolean active = true;

}