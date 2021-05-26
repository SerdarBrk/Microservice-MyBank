package com.serdarberk.accountservice.entity;

import javax.persistence.*;
import java.util.UUID;

import com.serdarberk.accountservice.dto.AccountDto;
import lombok.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID accountId;

    @Column(unique = true)
    private UUID iban;
    private UUID customerId;
    @Version
    private int version;
    @Enumerated(value = EnumType.STRING)
    private MoneyType moneyType;
    private double currency;


    public AccountDto toAccountDto() {
        return AccountDto.builder()
                .accountId(this.accountId)
                .iban(this.iban)
                .customerId(this.customerId)
                .moneyType(this.moneyType)
                .version(this.version)
                .currency(this.currency)
                .build();
    }
}
