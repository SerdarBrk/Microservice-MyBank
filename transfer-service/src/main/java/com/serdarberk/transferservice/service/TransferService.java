package com.serdarberk.transferservice.service;

import com.serdarberk.transferservice.VO.Account;
import com.serdarberk.transferservice.VO.Card;
import com.serdarberk.transferservice.VO.Transaction;
import com.serdarberk.transferservice.VO.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;


import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.*;


@Service
@Slf4j
public class TransferService {
    @Autowired
    private RestTemplate restTemplate;

    public void sendMoneyViaAccount(UUID accountId, UUID receiverAccountIban, double amount){
        Account senderAccount=restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/get/"+accountId,Account.class);
        Account receiverAccount=restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/getByIban/"+receiverAccountIban,Account.class);

 

        JSONObject jsonObject=new JSONObject(restTemplate
                .getForObject("https://api.exchangeratesapi.io/latest?base="+senderAccount.getMoneyType().toString()
                        +"&symbols="+receiverAccount.getMoneyType().toString(), String.class));
        double rate=jsonObject.getJSONObject("rates").getDouble(receiverAccount.getMoneyType().toString());
        if(senderAccount.getCurrency()<amount*rate)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is not enough money in the account");

        senderAccount.setCurrency(senderAccount.getCurrency()-(rate*amount));
        receiverAccount.setCurrency(receiverAccount.getCurrency()+(amount));
        List<Account> accountList = new ArrayList<>(Arrays.asList(senderAccount, receiverAccount));

        restTemplate.postForObject("http://ACCOUNT-SERVICE/api/accounts/updateAll",accountList, ResponseEntity.class);

        Transaction transactionSender=new Transaction();
        Transaction transactionReceiver =new Transaction();

        transactionSender.setTransactionDate(LocalDate.now());
        transactionSender.setPerformedId(accountId);
        transactionSender.setExplanation("Receiver: "+receiverAccountIban.toString()+" Amount: "+ amount);
        transactionSender.setTransactionType(TransactionType.TRANSFER);

        transactionReceiver.setTransactionDate(LocalDate.now());
        transactionReceiver.setPerformedId(receiverAccountIban);
        transactionReceiver.setExplanation("Sender: "+senderAccount.getIban()+" Amount: "+ amount);
        transactionReceiver.setTransactionType(TransactionType.TRANSFER);

        restTemplate.postForObject("http://TRANSACTION-SERVICE/api/transactions/",transactionSender,ResponseEntity.class);
        restTemplate.postForObject("http://TRANSACTION-SERVICE/api/transactions/",transactionReceiver,ResponseEntity.class);
    }

    public void sendMoneyViaCard(UUID cardNumber, UUID receiverAccountIban, double amount){
        Card senderCard=restTemplate.getForObject("http://CARD-SERVICE/api/cards/get/"+cardNumber,Card.class);
        Account senderAccount=restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/get/"+senderCard.getAccountId(),Account.class);
        Account receiverAccount=restTemplate.getForObject("http://ACCOUNT-SERVICE/api/accounts/getByIban/"+receiverAccountIban,Account.class);




        JSONObject jsonObject=new JSONObject(restTemplate
                .getForObject("http://api.exchangeratesapi.io/v1/latest?access_key=f4ab8dee5dc829e9882b8bfe26783aaa", String.class));
        double rate=jsonObject.getJSONObject("rates").getDouble(senderAccount.getMoneyType().toString());
        if(senderAccount.getCurrency()<amount*rate)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"There is not enough money in the account");

        senderAccount.setCurrency(senderAccount.getCurrency()-(rate*amount));
        receiverAccount.setCurrency(receiverAccount.getCurrency()+(amount));
        List<Account> accountList = new ArrayList<>(Arrays.asList(senderAccount, receiverAccount));

        restTemplate.postForObject("http://ACCOUNT-SERVICE/api/accounts/updateAll",accountList, ResponseEntity.class);

        Transaction transactionSender=new Transaction();
        Transaction transactionReceiver =new Transaction();

        transactionSender.setTransactionDate(LocalDate.now());
        transactionSender.setPerformedId(cardNumber);
        transactionSender.setExplanation("Receiver: "+receiverAccountIban.toString()+" Amount: "+ amount);
        transactionSender.setTransactionType(TransactionType.SHOPPING_CARD);

        transactionReceiver.setTransactionDate(LocalDate.now());
        transactionReceiver.setPerformedId(receiverAccountIban);
        transactionReceiver.setExplanation("Sender: "+cardNumber.toString()+" Amount: "+ amount);
        transactionReceiver.setTransactionType(TransactionType.TRANSFER);

        restTemplate.postForObject("http://TRANSACTION-SERVICE/api/transactions/",transactionSender,ResponseEntity.class);
        restTemplate.postForObject("http://TRANSACTION-SERVICE/api/transactions/",transactionReceiver,ResponseEntity.class);

    }
}
