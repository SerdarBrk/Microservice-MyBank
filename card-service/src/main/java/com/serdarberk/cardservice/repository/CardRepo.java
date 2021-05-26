package com.serdarberk.cardservice.repository;

import com.serdarberk.cardservice.entity.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CardRepo extends CrudRepository<Card, UUID> {

}