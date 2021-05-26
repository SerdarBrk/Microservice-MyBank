package com.serdarberk.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {
    @GetMapping("/customerServiceFallBack")
    public String userServiceFallBackMethod() {
        return "Customer Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/accountServiceFallBack")
    public String accountServiceFallBackMethod() {
        return "Account Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/cardServiceFallBack")
    public String cardServiceFallBackMethod() {
        return "Card Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/transactionServiceFallBack")
    public String transactionServiceFallBackMethod() {
        return "Transaction Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/transferServiceFallBack")
    public String transferServiceFallBackMethod() {
        return "Transfer Service is taking longer than Expected." +
                " Please try again later";
    }



    @GetMapping("/exchangerServiceFallBack")
    public String exchangerServiceFallBack() {
        return "Exchanger Service is taking longer than Expected." +
                " Please try again later";
    }
}
