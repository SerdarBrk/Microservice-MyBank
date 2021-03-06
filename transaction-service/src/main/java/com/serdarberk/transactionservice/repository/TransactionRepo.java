package com.serdarberk.transactionservice.repository;

import com.serdarberk.transactionservice.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepo extends CrudRepository<Transaction,Integer> {
    Page<Transaction> findAllByPerformedId(UUID performedId, Pageable pageable);
    void removeAllByPerformedId(UUID performedId);
}