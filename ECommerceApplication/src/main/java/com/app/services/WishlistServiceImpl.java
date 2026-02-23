package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Product;
import com.app.entites.User;
import com.app.entites.Wishlist;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.ProductDTO;
import com.app.repositories.ProductRepo;
import com.app.repositories.UserRepo;
import com.app.repositories.WishlistRepo;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private WishlistRepo wishlistRepo;

    @Override
    public ProductDTO addProductToWishlist(String email, Long productId) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (wishlistRepo.findByUserAndProduct(user, product).isPresent()) {
            return toDTO(product);
        }

        Wishlist wish = new Wishlist();
        wish.setUser(user);
        wish.setProduct(product);

        wishlistRepo.save(wish);

        return toDTO(product);
    }

    @Override
    public List<ProductDTO> getWishlistByUser(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<Wishlist> items = wishlistRepo.findByUser(user);

        return items.stream().map(w -> toDTO(w.getProduct())).collect(Collectors.toList());
    }

    @Override
    public String removeProductFromWishlist(String email, Long productId) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Wishlist wish = wishlistRepo.findByUserAndProduct(user, product)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "productId", productId));

        wishlistRepo.delete(wish);

        return "Product removed from wishlist";
    }

    private ProductDTO toDTO(Product p) {
        return new ProductDTO(p.getProductId(), p.getProductName(), p.getImage(), p.getDescription(), p.getQuantity(), p.getPrice(), p.getDiscount(), p.getSpecialPrice());
    }

}
