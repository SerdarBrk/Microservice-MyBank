package com.serdarberk.transactionservice.service;

import com.serdarberk.transactionservice.entity.Transaction;
import com.serdarberk.transactionservice.repository.TransactionRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepo transactionRepo;

    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }


    public void create(Transaction transaction){
         this.transactionRepo.save(transaction);
    }

    public Page<Transaction> summary(UUID performedid, Pageable pageable){
        return this.transactionRepo.findAllByPerformedId(performedid,pageable);
    }

    public void delete(UUID performedId){
        List<Transaction> transactionList=this.transactionRepo.findAllByPerformedId(performedId);
        if(transactionList != null){
            this.transactionRepo.deleteAll(transactionList);
        }
    }
    public Optional<Transaction> get(int transactionId){return this.transactionRepo.findById(transactionId);}
}
