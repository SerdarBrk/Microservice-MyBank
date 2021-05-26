package com.serdarberk.customerservice.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.serdarberk.customerservice.entity.Customer;
import com.serdarberk.customerservice.repository.CustomerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class CustomerService {
    private final CustomerRepo customerRepo;

    @Autowired
    private RestTemplate restTemplate;


    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Customer create(Customer customer) {
        log.info("Inside create of CustomerService");
        return this.customerRepo.save(customer);
    }

    public void delete(UUID customerId) {
        log.info("Inside delete of CustomerService");
        Customer customer = this.customerRepo.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with id" + customerId));
        ObjectNode objectNode = restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/hasCurrency/"+customer.getCustomerId().toString(),
                ObjectNode.class);
        if(objectNode.get("hasCurrency").toString().equals("true"))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"customer has amount of money");

        customerRepo.delete(customer);
    }

    public Customer updatePhone(UUID customerId, String phone) {
        log.info("Inside updatePhone of CustomerService");
        Customer customer = this.customerRepo.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found with id" + customerId));
        customer.setPhoneNumber(phone);
        return this.customerRepo.save(customer);
    }

    public ObjectNode hasCustomer(UUID customerId){
        log.info("Inside hasCustomer of CustomerService");
        ObjectNode objectNode= new ObjectNode(JsonNodeFactory.instance);
        boolean hasCustomer = customerRepo.hasCustomer(customerId);
        objectNode.put("hasCustomer",hasCustomer);
        return objectNode;

    }

    public Optional<Customer> get(UUID customerId) {

        log.info("Inside get of CustomerService");
        return this.customerRepo.findById(customerId);
    }
}