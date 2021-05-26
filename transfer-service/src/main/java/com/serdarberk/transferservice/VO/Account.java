package com.serdarberk.transferservice.VO;

import lombok.*;


import javax.persistence.Entity;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Account {

    private UUID accountId;
    private UUID iban;
    private UUID customerId;
    private int version;
    private MoneyType moneyType;
    private double currency;

}
