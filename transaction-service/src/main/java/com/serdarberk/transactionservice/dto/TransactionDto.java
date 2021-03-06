package com.serdarberk.transactionservice.dto;

import com.serdarberk.transactionservice.entity.Transaction;
import com.serdarberk.transactionservice.entity.TransactionType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class TransactionDto {
    private int id;
    private UUID performedId;
    private TransactionType transactionType;
    private String explanation;
    private LocalDate transactionDate;

    public Transaction toTransaction(){
        return Transaction.builder()
                .id(this.id)
                .performedId(this.performedId)
                .transactionType(this.transactionType)
                .explanation(this.explanation)
                .transactionDate(this.transactionDate)
                .build();
    }
}