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

import com.app.entites.Bank;
import com.app.payloads.BankDTO;
import com.app.services.BankService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class BankController {

	@Autowired
	private BankService bankService;

	/**
	 * Admin: Create bank
	 */
	@PostMapping("/admin/banks")
	public ResponseEntity<BankDTO> addBank(@Valid @RequestBody Bank bank) {
		BankDTO savedBank = bankService.addBank(bank);
		return new ResponseEntity<>(savedBank, HttpStatus.CREATED);
	}

	/**
	 * Public: Get all banks
	 */
	@GetMapping("/public/banks")
	public ResponseEntity<List<BankDTO>> getAllBanks() {
		List<BankDTO> banks = bankService.getBank();
		return new ResponseEntity<>(banks, HttpStatus.OK);
	}

	/**
	 * Public: Get bank by ID
	 */
	@GetMapping("/public/banks/{bankId}")
	public ResponseEntity<BankDTO> getBankById(@PathVariable Long bankId) {
		BankDTO bank = bankService.getBank(bankId);
		return new ResponseEntity<>(bank, HttpStatus.OK);
	}

	/**
	 * Public: Get bank by name
	 */
	@GetMapping("/public/banks/name/{bankName}")
	public ResponseEntity<BankDTO> getBankByName(@PathVariable String bankName) {
		BankDTO bank = bankService.getBank(bankName);
		return new ResponseEntity<>(bank, HttpStatus.OK);
	}

	/**
	 * Admin: Update bank
	 */
	@PutMapping("/admin/banks/{bankId}")
	public ResponseEntity<BankDTO> updateBank(
			@PathVariable Long bankId,
			@Valid @RequestBody Bank bank) {
		BankDTO updatedBank = bankService.updateBank(bankId, bank);
		return new ResponseEntity<>(updatedBank, HttpStatus.OK);
	}

	/**
	 * Admin: Delete bank
	 */
	@DeleteMapping("/admin/banks/{bankId}")
	public ResponseEntity<String> deleteBank(@PathVariable Long bankId) {
		String message = bankService.deleteBank(bankId);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

}