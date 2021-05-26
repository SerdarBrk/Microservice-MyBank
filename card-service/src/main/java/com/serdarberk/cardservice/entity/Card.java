package com.serdarberk.cardservice.entity;

import com.serdarberk.cardservice.dto.CardDto;
import lombok.*;
import javax.persistence.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue
    private UUID cardNumber;
    private UUID accountId;

    public CardDto toCardDto(){
        return CardDto.builder()
                .cardNumber(this.cardNumber)
                .accountId(this.accountId)
                .build();
    }

}