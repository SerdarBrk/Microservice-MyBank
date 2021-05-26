package com.serdarberk.customerservice.entity;

import com.serdarberk.customerservice.dto.CustomerDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")

public class Customer {
    @Id
    @GeneratedValue
    private UUID customerId;

    @Column(unique = true)
    private String tc;

    private String name;

    private String surname;

    @Column(unique = true)
    private String phoneNumber;


    public CustomerDto toCustomerDto() {
        return CustomerDto.builder()
                .customerId(this.customerId)
                .tc(this.tc)
                .name(this.name)
                .surname(this.surname)
                .phoneNumber(this.phoneNumber)
                .build();
    }

}

