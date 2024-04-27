package com.techxnet.munshi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techxnet.munshi.daos.CustomerRepository;
import com.techxnet.munshi.models.Customer;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();
        return customers;
    }

    public Customer getCustomer(int id) {
        Customer customer = customerRepo.findById(id).get();
        return customer;
    }

    public Customer upsertCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public void deleteCustomer(int id) {
        customerRepo.deleteById(id);
    }
}
