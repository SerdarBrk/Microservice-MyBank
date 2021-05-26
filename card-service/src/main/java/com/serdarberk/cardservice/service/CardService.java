package com.serdarberk.cardservice.service;

import com.serdarberk.cardservice.entity.Card;
import com.serdarberk.cardservice.repository.CardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;



import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {
    @Autowired
    private RestTemplate restTemplate;
    private final CardRepo cardRepo;

    public CardService(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }


    public Card create(Card card){

        return this.cardRepo.save(card);
    }


    public void shopping(UUID cardNumber,UUID receiverIban,double money){

        Card debitCard=this.cardRepo.findById(cardNumber)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Card not found with number"+cardNumber));

       if (money<0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Money must be greater than 0: "+money);
        restTemplate.postForObject("http://TRANSFER-SERICE/api/transfer/sendMoneyViaCard?cardNumber="+cardNumber.toString()+
                "&receiverAccountIban="+receiverIban+"&money="+money,null, ResponseEntity.class);
    }

    public void delete(UUID debitcardNumber){
        Card debitCard=this.cardRepo.findById(debitcardNumber)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND
                        ,"Debitcard not found with number: "+debitcardNumber));


        this.cardRepo.delete(debitCard);
      /*  List<Transaction> transactions=this.transactionRepo.findAllByPerformedId(debitcardNumber);
        if(transactions != null){
            this.transactionRepo.deleteAll(transactions);
        }*/

    }
    public Optional<Card> get(UUID debitcardNumber){return this.cardRepo.findById(debitcardNumber);}
}
