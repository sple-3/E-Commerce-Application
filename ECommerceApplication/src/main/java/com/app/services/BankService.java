package com.app.services;

import java.util.List;

import com.app.entites.Bank;
import com.app.payloads.BankDTO;

public interface BankService {
	
	BankDTO addBank(Bank promoCode);
	
	List<BankDTO> getBank();
	
	BankDTO getBank(Long bankId);

    BankDTO getBank(String bankName);
	
	BankDTO updateBank(Long bankId, Bank bank);
	
	String deleteBank(Long bankId);
}
