package com.serdarberk.customerservice.controller;

import com.serdarberk.customerservice.dto.CustomerDto;
import com.serdarberk.customerservice.service.CustomerService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Validated
@RestController
@Slf4j
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto create(@Valid @RequestBody CustomerDto customerDto){
        log.info("Inside create of CustomerController");

        return this.customerService.create(customerDto.toCustomer()).toCustomerDto();
    }
    @PostMapping("/profile/{customerId}/updatePhone")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CustomerDto updatePhone(@PathVariable("customerId") UUID customerId,
                                   @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
                                           message = "Phone Number Ex:\"+111 (202) 555-0125\"")
                                   @RequestParam("phoneNumber") String phoneNumber){
        log.info("Inside updatePhone of CustomerController");

        return this.customerService.updatePhone(customerId, phoneNumber).toCustomerDto();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam("customerId") UUID customerId){

        log.info("Inside delete of CustomerController");
        this.customerService.delete(customerId);
    }

    @GetMapping("/hasCustomer")
    public ObjectNode hasCustomer(@RequestParam("customerId") UUID customerId){
        log.info("Inside hasCustomer of CustomerController");

        return customerService.hasCustomer(customerId);
    }

    @GetMapping("/get/{customerId}")
    public CustomerDto get(@PathVariable("customerId") UUID customerId){
        log.info("Inside get of CustomerController");

        return this.customerService.get(customerId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer not found with id"+customerId)).toCustomerDto();
    }
}