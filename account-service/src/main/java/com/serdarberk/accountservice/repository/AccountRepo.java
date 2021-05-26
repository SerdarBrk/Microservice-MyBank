package com.serdarberk.accountservice.repository;

import com.serdarberk.accountservice.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepo extends CrudRepository<Account, UUID> {
    Page<Account> findAllByCustomerId(UUID customerId, Pageable pageable);

    @Query("select case  when sum(account.currency) = 0 then true else false end from Account account where account.customerId = :customerId")
    boolean hasCurrency(@Param("customerId") UUID customerId);
    @Query("select case when count(account)> 0 then true else false end from Account account where account.customerId =:customerId")
    boolean hasCustomer(@Param("customerId") UUID customerId);

    Optional<Account> findByIban(UUID iban);
    void deleteAllByCustomerId(UUID customerID);
}