package com.serdarberk.transferservice.VO;

import lombok.*;


import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private int id;
    private UUID performedId;
    private TransactionType transactionType;
    private String explanation;
    private LocalDate transactionDate;
}
