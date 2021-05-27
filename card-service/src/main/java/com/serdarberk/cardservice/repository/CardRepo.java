package com.serdarberk.cardservice.repository;

import com.serdarberk.cardservice.entity.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CardRepo extends CrudRepository<Card, UUID> {
    void removeAllByCustomerId(UUID customerID);
    void removeByAccountId(UUID accountId);
    @Query("select case when count(card)> 0 then true else false end from Card card where card.customerId =:customerId")
    boolean hasCustomer(@Param("customerId") UUID customerId);


}