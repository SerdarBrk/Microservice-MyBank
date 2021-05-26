package com.serdarberk.customerservice.repository;

import com.serdarberk.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CustomerRepo extends CrudRepository<Customer, UUID> {
    @Query("select case when count(customer)> 0 then true else false end from Customer customer where customer.customerId =?1")
    boolean hasCustomer(@Param("customerId") UUID customerId);
}