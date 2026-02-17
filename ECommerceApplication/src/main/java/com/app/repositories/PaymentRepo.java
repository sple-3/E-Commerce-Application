package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.Payment;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long>{
    
    @Query("SELECT p FROM Payment p WHERE p.order.orderId = ?1")
	Payment findByOrderId(Long orderId);
}
