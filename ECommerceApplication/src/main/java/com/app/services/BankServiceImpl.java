package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Bank;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.BankDTO;
import com.app.repositories.BankRepo;

@Service
public class BankServiceImpl implements BankService {

	@Autowired
	private BankRepo bankRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public BankDTO addBank(Bank bank) {
		// Check if bank already exists
		Bank existingBank = bankRepo.findByBankName(bank.getBankName());
		if (existingBank != null) {
			throw new APIException("Bank " + bank.getBankName() + " already exists");
		}

		Bank savedBank = bankRepo.save(bank);
		return modelMapper.map(savedBank, BankDTO.class);
	}

	@Override
	public List<BankDTO> getBank() {
		List<Bank> banks = bankRepo.findAll();

		return banks.stream()
				.map(bank -> modelMapper.map(bank, BankDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public BankDTO getBank(Long bankId) {
		Bank bank = bankRepo.findById(bankId)
				.orElseThrow(() -> new ResourceNotFoundException("Bank", "bankId", bankId));

		return modelMapper.map(bank, BankDTO.class);
	}

	@Override
	public BankDTO getBank(String bankName) {
		Bank bank = bankRepo.findByBankName(bankName);
		
		if (bank == null) {
			throw new ResourceNotFoundException("Bank", "bankName", bankName);
		}

		return modelMapper.map(bank, BankDTO.class);
	}

	@Override
	public BankDTO updateBank(Long bankId, Bank bank) {
		Bank existingBank = bankRepo.findById(bankId)
				.orElseThrow(() -> new ResourceNotFoundException("Bank", "bankId", bankId));

		existingBank.setBankName(bank.getBankName());
		existingBank.setAccountNumber(bank.getAccountNumber());

		Bank updatedBank = bankRepo.save(existingBank);
		return modelMapper.map(updatedBank, BankDTO.class);
	}

	@Override
	public String deleteBank(Long bankId) {
		Bank bank = bankRepo.findById(bankId)
				.orElseThrow(() -> new ResourceNotFoundException("Bank", "bankId", bankId));

		bankRepo.delete(bank);

		return "Bank with bankId: " + bankId + " deleted successfully";
	}

}