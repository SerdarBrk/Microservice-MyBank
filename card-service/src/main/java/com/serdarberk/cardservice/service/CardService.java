package com.serdarberk.cardservice.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.serdarberk.cardservice.entity.Card;
import com.serdarberk.cardservice.repository.CardRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;



import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CardService {
    @Autowired
    private RestTemplate restTemplate;
    private final CardRepo cardRepo;

    public CardService(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }


    public Card create(Card card){
        log.info("Inside create of CardService");
        ObjectNode hasCustomer=restTemplate.getForObject("http://CUSTOMER-SERVICE/api/customers/hasCustomer?customerId="
                +card.getCustomerId(),ObjectNode.class);
        ObjectNode hasAccount=restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/hasAccount?accountId="
                +card.getAccountId(),ObjectNode.class);

        if(hasCustomer.get("hasCustomer").toString().equals("false"))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"customer not found");

        if(hasAccount.get("hasAccount").toString().equals("false"))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"account not found");

        return this.cardRepo.save(card);
    }

    @Transactional
    public void shopping(UUID cardNumber,UUID receiverIban,double money){
        log.info("Inside shopping of CardService");
        Card debitCard=this.cardRepo.findById(cardNumber)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Card not found with number"+cardNumber));

       if (money<0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Money must be greater than 0: "+money);
        restTemplate.postForObject("http://TRANSFER-SERVICE/sendMoneyViaCard?cardNumber="+cardNumber+
                "&receiverAccountIban="+receiverIban+"&money="+money,null, ResponseEntity.class);
    }
    @Transactional
    public void delete(UUID debitcardNumber){
        log.info("Inside delete of CardService");
        Card debitCard=this.cardRepo.findById(debitcardNumber)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND
                        ,"Debitcard not found with number: "+debitcardNumber));

        this.cardRepo.delete(debitCard);
    }
    @Transactional
    public void deleteByAccountId(UUID accountId){
        log.info("Inside deleteByAccountId of CardService");
        this.cardRepo.removeByAccountId(accountId);
    }
    @Transactional
    public void deleteAll(UUID customerId){
        log.info("Inside deleteAll of CardService");
        this.cardRepo.removeAllByCustomerId(customerId);
    }
    @Transactional
    public Optional<Card> get(UUID debitcardNumber){
        log.info("Inside get of CardService");
        return this.cardRepo.findById(debitcardNumber);
    }
}
