package com.app.services;

import java.util.List;

import com.app.payloads.ProductDTO;

public interface WishlistService {

    ProductDTO addProductToWishlist(String email, Long productId);

    List<ProductDTO> getWishlistByUser(String email);

    String removeProductFromWishlist(String email, Long productId);

}
