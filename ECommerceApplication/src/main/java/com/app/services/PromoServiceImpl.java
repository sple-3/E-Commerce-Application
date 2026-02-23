package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Promo;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.PromoDTO;
import com.app.repositories.PromoRepo;

@Service
public class PromoServiceImpl implements PromoService {

	@Autowired
	private PromoRepo promoRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PromoDTO addPromo(Promo promo) {
		// Check if promo code already exists
		if (promoRepo.existsById(promo.getPromoCode())) {
			throw new APIException("Promo code " + promo.getPromoCode() + " already exists");
		}

		Promo savedPromo = promoRepo.save(promo);
		return modelMapper.map(savedPromo, PromoDTO.class);
	}

	@Override
	public List<PromoDTO> getPromo() {
		List<Promo> promos = promoRepo.findAll();

		return promos.stream()
				.map(promo -> modelMapper.map(promo, PromoDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public PromoDTO getPromo(String promoCode) {
		Promo promo = promoRepo.findById(promoCode)
				.orElseThrow(() -> new ResourceNotFoundException("Promo", "promoCode", promoCode));

		return modelMapper.map(promo, PromoDTO.class);
	}

	@Override
	public int getPromoCount(String promoCode) {
		Promo promo = promoRepo.findById(promoCode)
				.orElseThrow(() -> new ResourceNotFoundException("Promo", "promoCode", promoCode));

		return promo.getCounter();
	}

	@Override
	public PromoDTO updatePromo(String promoCode, Promo promo) {
		Promo existingPromo = promoRepo.findById(promoCode)
				.orElseThrow(() -> new ResourceNotFoundException("Promo", "promoCode", promoCode));

		existingPromo.setDiscount(promo.getDiscount());

		Promo updatedPromo = promoRepo.save(existingPromo);
		return modelMapper.map(updatedPromo, PromoDTO.class);
	}

	@Override
	public String deletePromo(String promoCode) {
		Promo promo = promoRepo.findById(promoCode)
				.orElseThrow(() -> new ResourceNotFoundException("Promo", "promoCode", promoCode));

		promoRepo.delete(promo);

		return "Promo code " + promoCode + " deleted successfully";
	}

}