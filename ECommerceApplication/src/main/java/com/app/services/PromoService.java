package com.app.services;

import java.util.List;

import com.app.entites.Promo;
import com.app.payloads.PromoDTO;

public interface PromoService {
	
	PromoDTO addPromo(Promo promoCode);
	
	List<PromoDTO> getPromo();
	
	PromoDTO getPromo(String promoCode);

	int getPromoCount(String promoCode);
	
	PromoDTO updatePromo(String promoCode, Promo promo);
	
	String deletePromo(String promoCode);
}
