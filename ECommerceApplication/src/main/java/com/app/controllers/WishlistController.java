package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.ProductDTO;
import com.app.services.WishlistService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/public/users/{email}/wishlist/products/{productId}")
    public ResponseEntity<ProductDTO> addToWishlist(@PathVariable String email, @PathVariable Long productId) {
        ProductDTO dto = wishlistService.addProductToWishlist(email, productId);
        return new ResponseEntity<ProductDTO>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/public/users/{email}/wishlist")
    public ResponseEntity<List<ProductDTO>> getWishlist(@PathVariable String email) {
        List<ProductDTO> list = wishlistService.getWishlistByUser(email);
        return new ResponseEntity<List<ProductDTO>>(list, HttpStatus.OK);
    }

    @DeleteMapping("/public/users/{email}/wishlist/products/{productId}")
    public ResponseEntity<String> removeFromWishlist(@PathVariable String email, @PathVariable Long productId) {
        String res = wishlistService.removeProductFromWishlist(email, productId);
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }

}
