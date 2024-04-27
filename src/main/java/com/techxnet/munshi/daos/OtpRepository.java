package com.techxnet.munshi.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techxnet.munshi.models.Otp;

public interface OtpRepository extends JpaRepository<Otp, Integer>{}
