package com.serdarberk.transferservice.service;

import com.serdarberk.transferservice.VO.*;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.*;


@Service
@Slf4j
public class TransferService {
    @Autowired
    private RestTemplate restTemplate;

    private JSONObject jsonObject;

    public void sendMoneyViaAccount(UUID accountId, UUID receiverAccountIban, double amount){
        log.info("Inside sendMoneyViaAccount of TransferService");
        Account senderAccount=restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/get/"+accountId,Account.class);
        Account receiverAccount=restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/getByIban/"+receiverAccountIban,Account.class);

        if(senderAccount.getCurrency()<amount)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is not enough money in the account");

        if(senderAccount.getMoneyType().equals(receiverAccount.getMoneyType())){

            senderAccount.setCurrency(senderAccount.getCurrency()-(amount));
            receiverAccount.setCurrency(receiverAccount.getCurrency()+(amount));
        }else if(senderAccount.getMoneyType().equals(MoneyType.EUR)){
            jsonObject=new JSONObject(restTemplate
                    .getForObject("http://api.exchangeratesapi.io/v1/latest?access_key=f4ab8dee5dc829e9882b8bfe26783aaa", String.class));
            double rate=jsonObject.getJSONObject("rates").getDouble(receiverAccount.getMoneyType().toString());

            senderAccount.setCurrency(senderAccount.getCurrency()-(amount));
            receiverAccount.setCurrency(receiverAccount.getCurrency()+(amount*rate));
        }else{
            jsonObject=new JSONObject(restTemplate
                    .getForObject("http://api.exchangeratesapi.io/v1/latest?access_key=f4ab8dee5dc829e9882b8bfe26783aaa", String.class));
            double rate=jsonObject.getJSONObject("rates").getDouble(senderAccount.getMoneyType().toString());

            senderAccount.setCurrency(senderAccount.getCurrency()-(amount));
            receiverAccount.setCurrency(receiverAccount.getCurrency()+(amount/rate));
        }

        List<Account> accountList = new ArrayList<>(Arrays.asList(senderAccount, receiverAccount));

        restTemplate.postForObject("http://ACCOUNT-SERVICE/api/accounts/updateAll",accountList, ResponseEntity.class);

        Transaction transactionSender=new Transaction();
        Transaction transactionReceiver =new Transaction();

        transactionSender.setTransactionDate(LocalDate.now());
        transactionSender.setPerformedId(senderAccount.getCustomerId());
        transactionSender.setExplanation("Account id: "+accountId+"ReceiverIan: "+receiverAccountIban+" Amount: "+ amount);
        transactionSender.setTransactionType(TransactionType.TRANSFER);

        transactionReceiver.setTransactionDate(LocalDate.now());
        transactionReceiver.setPerformedId(receiverAccountIban);
        transactionReceiver.setExplanation("Account id"+receiverAccount.getAccountId()+"Sender: "+senderAccount.getIban()+" Amount: "+ amount);
        transactionReceiver.setTransactionType(TransactionType.TRANSFER);

        restTemplate.postForObject("http://TRANSACTION-SERVICE/api/transactions/",transactionSender,ResponseEntity.class);
        restTemplate.postForObject("http://TRANSACTION-SERVICE/api/transactions/",transactionReceiver,ResponseEntity.class);
    }

    public void sendMoneyViaCard(UUID cardNumber, UUID receiverAccountIban, double amount){
        log.info("Inside sendMoneyViaCard of TransferService");
        Card senderCard=restTemplate.getForObject("http://CARD-SERVICE/api/cards/get/"+cardNumber,Card.class);
        Account senderAccount=restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/get/"+senderCard.getAccountId(),Account.class);
        Account receiverAccount=restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/getByIban/"+receiverAccountIban,Account.class);

        if(senderAccount.getCurrency()<amount)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is not enough money in the account");

        if(senderAccount.getMoneyType().equals(receiverAccount.getMoneyType())){
            senderAccount.setCurrency(senderAccount.getCurrency()-(amount));
            receiverAccount.setCurrency(receiverAccount.getCurrency()+(amount));
        }else if(senderAccount.getMoneyType().equals(MoneyType.EUR)){
            jsonObject=new JSONObject(restTemplate
                    .getForObject("http://api.exchangeratesapi.io/v1/latest?access_key=f4ab8dee5dc829e9882b8bfe26783aaa", String.class));
            double rate=jsonObject.getJSONObject("rates").getDouble(receiverAccount.getMoneyType().toString());
            senderAccount.setCurrency(senderAccount.getCurrency()-(amount));
            receiverAccount.setCurrency(receiverAccount.getCurrency()+(rate*amount));
        }else{
            jsonObject=new JSONObject(restTemplate
                    .getForObject("http://api.exchangeratesapi.io/v1/latest?access_key=f4ab8dee5dc829e9882b8bfe26783aaa", String.class));
            double rate=jsonObject.getJSONObject("rates").getDouble(senderAccount.getMoneyType().toString());
            senderAccount.setCurrency(senderAccount.getCurrency()-(amount));
            receiverAccount.setCurrency(receiverAccount.getCurrency()+(amount/rate));
        }
        List<Account> accountList = new ArrayList<>(Arrays.asList(senderAccount, receiverAccount));
        restTemplate.postForObject("http://ACCOUNT-SERVICE/api/accounts/updateAll",accountList, ResponseEntity.class);

        Transaction transactionSender=new Transaction();
        Transaction transactionReceiver =new Transaction();

        transactionSender.setTransactionDate(LocalDate.now());
        transactionSender.setPerformedId(senderCard.getCustomerId());
        transactionSender.setExplanation("Card number:"+cardNumber+"Receiver: "+receiverAccountIban+" Amount: "+ amount);
        transactionSender.setTransactionType(TransactionType.SHOPPING_CARD);

        transactionReceiver.setTransactionDate(LocalDate.now());
        transactionReceiver.setPerformedId(receiverAccount.getCustomerId());
        transactionReceiver.setExplanation("Account id:"+receiverAccount.getAccountId()+"Sender: "+cardNumber+" Amount: "+ amount);
        transactionReceiver.setTransactionType(TransactionType.TRANSFER);

        restTemplate.postForObject("http://TRANSACTION-SERVICE/api/transactions/",transactionSender,ResponseEntity.class);
        restTemplate.postForObject("http://TRANSACTION-SERVICE/api/transactions/",transactionReceiver,ResponseEntity.class);

    }
}
