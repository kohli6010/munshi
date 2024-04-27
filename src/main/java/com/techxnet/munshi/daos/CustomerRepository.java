package com.techxnet.munshi.daos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techxnet.munshi.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
