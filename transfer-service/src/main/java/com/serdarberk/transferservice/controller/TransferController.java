package com.serdarberk.transferservice.controller;

import com.serdarberk.transferservice.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController("/api/transfer")
public class TransferController {
    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/sendMoneyViaAccount")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendMoneyViaAccount(@RequestParam("accountId")UUID accountId,
                                    @RequestParam("receiverAccountIban") UUID receiverAccountIban,
                                    @RequestParam("money") double money){
        log.info("Inside sendMoneyViaAccount of CardController");
        this.transferService.sendMoneyViaAccount(accountId,receiverAccountIban,money);
    }

    @PostMapping("/sendMoneyViaCard")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendMoneyViaCard(@RequestParam("cardNumber")UUID cardNumber,
                                    @RequestParam("receiverAccountIban") UUID receiverAccountIban,
                                    @RequestParam("money") double money){
        log.info("Inside sendMoneyViaCard of CardController");
        this.transferService.sendMoneyViaCard(cardNumber,receiverAccountIban,money);
    }
}
