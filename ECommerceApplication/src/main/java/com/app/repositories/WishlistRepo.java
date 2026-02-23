package com.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.Product;
import com.app.entites.User;
import com.app.entites.Wishlist;

@Repository
public interface WishlistRepo extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser(User user);

    Optional<Wishlist> findByUserAndProduct(User user, Product product);

}
