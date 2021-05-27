package com.serdarberk.transactionservice.controller;

import com.serdarberk.transactionservice.dto.TransactionDto;
import com.serdarberk.transactionservice.entity.Transaction;
import com.serdarberk.transactionservice.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
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
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody TransactionDto transactionDto){
        this.transactionService.create(transactionDto.toTransaction());
    }

    @GetMapping(value = "/listByPerformedId",params = {"performedId", "page", "size"})
    public List<TransactionDto> listByPerformedId(@RequestParam("performedId") UUID performedId
            , @Min(value = 0) @RequestParam("page") int page, @RequestParam("size") int size) {

        return transactionService.summary(performedId, PageRequest.of(page, size)).stream()
                .map(Transaction::toTransactionDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam("performedId") UUID performedId){
        this.transactionService.delete(performedId);
    }

    @GetMapping("/{transactionId}")
    public TransactionDto get(@RequestParam("transactionId") int transactionId){
        return this.transactionService.get(transactionId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Transaction not found with id: "+transactionId))
                .toTransactionDto();
    }
}