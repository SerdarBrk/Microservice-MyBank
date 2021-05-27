package com.serdarberk.transferservice.VO;

import lombok.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    private UUID cardNumber;
    private UUID accountId;
    private UUID customerId;

}
