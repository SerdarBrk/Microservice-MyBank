package com.serdarberk.accountservice.dto;

import com.serdarberk.accountservice.entity.Account;
import com.serdarberk.accountservice.entity.MoneyType;
import lombok.*;
import org.springframework.data.annotation.Version;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Builder
public class AccountDto {
    private UUID accountId;
    private UUID iban;
    private UUID customerId;
    private MoneyType moneyType;
    @Version
    private int version;
    @Min(value = 0,message = "currency cannot be less than 0")
    private double currency;

    public Account toAccount() {
        return Account.builder()
                .accountId(this.accountId)
                .iban(this.iban)
                .customerId(this.customerId)
                .moneyType(this.moneyType)
                .version(this.version)
                .currency(this.currency)
                .build();
    }
}