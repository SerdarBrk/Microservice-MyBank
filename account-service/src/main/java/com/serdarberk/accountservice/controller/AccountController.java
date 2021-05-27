package com.serdarberk.accountservice.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.serdarberk.accountservice.dto.AccountDto;
import com.serdarberk.accountservice.entity.Account;
import com.serdarberk.accountservice.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {this.accountService = accountService;}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto create(@Valid @RequestBody AccountDto accountDto){
        log.info("Inside create of AccountController");
        return this.accountService.create(accountDto.toAccount()).toAccountDto();
    }

    @GetMapping("/listByCustomer")
    public List<AccountDto> listByCustomer(@RequestParam("customerId") UUID customerId, @Min(value = 0) @RequestParam("page") int page, @RequestParam("size") int size) {
        log.info("Inside listByCustomer of AccountController");
        return accountService.list(customerId, PageRequest.of(page, size)).stream()
                .map(Account::toAccountDto)
                .collect(Collectors.toList());
    }
    @PostMapping("/{accountId}/sendMoney")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendMoney(@PathVariable("accountId") UUID accountId,
                                @RequestParam("receiverIban") UUID receiverIban,
                                @Min(value = 0,message = "money cannot be less than 0")
                                @RequestParam("money") double money){
        log.info("Inside sendMoney of AccountController");

         this.accountService.sendMoney(accountId,receiverIban,money);
    }

    @PostMapping("/updateAll")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateAll(@RequestBody List<AccountDto> accountDto){
        this.accountService.updateAll(accountDto.stream().map(AccountDto::toAccount).collect(Collectors.toList()));
    }

    @DeleteMapping(value = "/delete",params = {"accountId"})
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam("accountId") UUID accountId){
        log.info("Inside delete of AccountController");
        accountService.delete(accountId);
    }

    @DeleteMapping(value = "/deleteAll",params = {"customerId"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteAll(@RequestParam("customerId") UUID customerId){
        log.info("Inside deleteAll of AccountController");

        accountService.deleteAll(customerId);
    }

    @GetMapping("/hasAccount")
    public ObjectNode hasCustomer(@RequestParam("accountId") UUID accountId){
        return this.accountService.hasAccount(accountId);
    }

    @GetMapping("/getByIban/{accountIban}")
    public AccountDto getByIban(@PathVariable("accountIban") UUID accountIban){
        log.info("Inside get of AccountController");

        return this.accountService.getByIban(accountIban)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found with iban"+accountIban)).toAccountDto();
    }
    @GetMapping("/get/{accountId}")
    public AccountDto get(@PathVariable("accountId") UUID accountId){
        log.info("Inside get of AccountController");

        return this.accountService.get(accountId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found with id"+accountId)).toAccountDto();
    }
}
