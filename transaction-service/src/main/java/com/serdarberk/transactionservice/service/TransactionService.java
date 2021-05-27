package com.serdarberk.transactionservice.service;

import com.serdarberk.transactionservice.entity.Transaction;
import com.serdarberk.transactionservice.repository.TransactionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Slf4j

@Service
public class TransactionService {
    private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @Transactional
    public void create(Transaction transaction){
         log.info("Inside create of TransactionService");
         this.transactionRepo.save(transaction);
    }
    @Transactional
    public Page<Transaction> summary(UUID performedid, Pageable pageable){
        log.info("Inside summary of TransactionService");
        return this.transactionRepo.findAllByPerformedId(performedid,pageable);
    }
    @Transactional
    public void delete(UUID performedId){
        log.info("Inside delete of TransactionService");
        this.transactionRepo.removeAllByPerformedId(performedId);
    }

    @Transactional
    public Optional<Transaction> get(int transactionId){
        log.info("Inside get of TransactionService");
        return this.transactionRepo.findById(transactionId);}
}
