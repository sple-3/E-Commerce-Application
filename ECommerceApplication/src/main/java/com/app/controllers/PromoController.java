package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.Promo;
import com.app.payloads.PromoDTO;
import com.app.services.PromoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class PromoController {

	@Autowired
	private PromoService promoService;

	/**
	 * Admin: Create promo code
	 */
	@PostMapping("/admin/promo")
	public ResponseEntity<PromoDTO> addPromo(@Valid @RequestBody Promo promo) {
		PromoDTO savedPromo = promoService.addPromo(promo);
		return new ResponseEntity<>(savedPromo, HttpStatus.CREATED);
	}

	/**
	 * Public: Get all promo codes
	 */
	@GetMapping("/public/promo")
	public ResponseEntity<List<PromoDTO>> getAllPromos() {
		List<PromoDTO> promos = promoService.getPromo();
		return new ResponseEntity<>(promos, HttpStatus.OK);
	}

	/**
	 * Public: Get promo by code
	 */
	@GetMapping("/public/promo/{promoCode}")
	public ResponseEntity<PromoDTO> getPromo(@PathVariable String promoCode) {
		PromoDTO promoDTO = promoService.getPromo(promoCode);
		return new ResponseEntity<>(promoDTO, HttpStatus.OK);
	}

	/**
	 * Admin: Get promo count by code
	 */
	@GetMapping("/admin/promo/count/{promoCode}")
	public ResponseEntity<Integer> getPromoCode(@PathVariable String promoCode) {
		int promoCount = promoService.getPromoCount(promoCode);
		return new ResponseEntity<>(promoCount, HttpStatus.OK);
	}

	/**
	 * Admin: Update promo code
	 */
	@PutMapping("/admin/promo/{promoCode}")
	public ResponseEntity<PromoDTO> updatePromo(
			@PathVariable String promoCode,
			@Valid @RequestBody Promo promo) {
		PromoDTO updatedPromo = promoService.updatePromo(promoCode, promo);
		return new ResponseEntity<>(updatedPromo, HttpStatus.OK);
	}

	/**
	 * Admin: Delete promo code
	 */
	@DeleteMapping("/admin/promo/{promoCode}")
	public ResponseEntity<String> deletePromo(@PathVariable String promoCode) {
		String message = promoService.deletePromo(promoCode);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

}