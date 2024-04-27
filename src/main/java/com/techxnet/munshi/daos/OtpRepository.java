package com.techxnet.munshi.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techxnet.munshi.models.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Integer>{
    Optional<Otp> findByCustomerIdOrderByCreatedAtDesc(int customerId);
}
