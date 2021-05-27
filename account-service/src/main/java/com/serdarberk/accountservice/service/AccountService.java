package com.serdarberk.accountservice.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.serdarberk.accountservice.entity.Account;
import com.serdarberk.accountservice.repository.AccountRepo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;
@Slf4j
@Service
public class AccountService {


    private final AccountRepo accountRepo;
    @Autowired
    private RestTemplate restTemplate;
    public AccountService(AccountRepo accountRepo) {

        this.accountRepo = accountRepo;
    }

    public Account create(Account account){
        log.info("Inside create of AccountService");
        ObjectNode hasCustomer=restTemplate.getForObject("http://CUSTOMER-SERVICE/api/customers/hasCustomer?customerId="
                +account.getCustomerId(),ObjectNode.class);

            if(hasCustomer.get("hasCustomer").toString().equals("false"))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"customer not found");
        return this.accountRepo.save(account);
    }
    @Transactional
    public Page<Account> list(UUID customerId, Pageable pageable){
        log.info("Inside list of AccountService");

        return this.accountRepo.findAllByCustomerId(customerId,pageable);
    }
    @Transactional
    public void sendMoney(UUID accountId,UUID receiverIban,double money){
        log.info("Inside sendMoney of AccountService");

        restTemplate.postForObject("http://TRANSFER-SERVICE/sendMoneyViaAccount?accountId="+accountId+
                "&receiverAccountIban="+receiverIban+"&money="+money,null,ResponseEntity.class);
    }

    @Transactional
    public void delete(UUID accountId){
        log.info("Inside delete of AccountService");

        Account account=this.accountRepo.findById(accountId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account not found with accountId: "+accountId));
        this.restTemplate.delete("http://CARD-SERVICE/deleteByAccountId?accountId="+accountId);
        if(account.getCurrency() != 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is money in the account. Account cannot be deleted");
        this.accountRepo.delete(account);

    }
    @Transactional
    public void updateAll(List<Account> account)
    {
        this.accountRepo.saveAll(account);
    }

    @Transactional
    public void deleteAll(UUID customerId){

        if(accountRepo.hasCurrency(customerId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is money in the account. Account cannot be deleted");
        this.accountRepo.removeAllByCustomerId(customerId);
    }
    @Transactional
    public ObjectNode hasAccount(UUID accountId){
        ObjectNode objectNode=new ObjectNode(JsonNodeFactory.instance);
        objectNode.put("hasAccount",this.accountRepo.hasAccount(accountId));
        return objectNode;
    }
    @Transactional
    public Optional<Account> getByIban(UUID accountId){
        log.info("Inside get of AccountService");
        return this.accountRepo.findByIban(accountId);
    }
    @Transactional
    public Optional<Account> get(UUID accountId){
        log.info("Inside get of AccountService");
        return this.accountRepo.findById(accountId);
    }

}
