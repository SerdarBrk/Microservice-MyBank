package com.serdarberk.cardservice.dto;
import com.serdarberk.cardservice.entity.Card;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@Builder
public class CardDto {
    private UUID cardNumber;
    @NotBlank(message =  "Account for the card is mandatory")
    private UUID accountId;

    public Card toCard(){
        return Card.builder()
                .cardNumber(this.cardNumber)
                .accountId(this.accountId)
                .build();
    }
}