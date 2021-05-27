package com.serdarberk.cardservice.controller;

import com.serdarberk.cardservice.dto.CardDto;
import com.serdarberk.cardservice.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardDto create(@Valid @RequestBody CardDto cardDto){

        return this.cardService.create(cardDto.toCard()).toCardDto();
    }
    @PostMapping("/shopping")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void shopping(@RequestParam("cardNumber") UUID cardNumber,
                                 @RequestParam("receiverIban") UUID receiverIban,
                                 @RequestParam("money") double money){
        this.cardService.shopping(cardNumber, receiverIban, money);
    }
    @DeleteMapping(value = "/deleteAll")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAll(@RequestParam("customerId") UUID customerId){
        this.cardService.deleteAll(customerId);
    }
    @DeleteMapping(value = "/deleteByAccountId")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByAccountId(@RequestParam("accountId") UUID accountId){
        this.cardService.deleteByAccountId(accountId);
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam("cardNumber") UUID cardNumber){
        this.cardService.delete(cardNumber);
    }
    @GetMapping("/get/{cardNumber}")
    public CardDto get(@PathVariable("cardNumber") UUID cardNumber){
        return this.cardService.get(cardNumber)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Card not found with number: "+cardNumber))
                .toCardDto();
    }
}