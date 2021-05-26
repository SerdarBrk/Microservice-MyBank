package com.serdarberk.customerservice.dto;

import com.serdarberk.customerservice.entity.Customer;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@Builder
public class CustomerDto {

    private UUID customerId;
    @NotBlank(message = "TC for the customer is mandatory")
    @Pattern(regexp = "^[0-9]{11}",message = "TC  length must be 11 and TC contains only alphanumeric characters.")
    private String tc;
    @NotBlank(message = "Name for the customer is mandatory")
    private String name;
    @NotBlank(message = "Surname for the customer is mandatory")
    private String surname;
    @NotBlank(message = "PhoneNumber for the customer is mandatory. Ex:+(123)-456-78-90")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",message = "Phone Number Ex:\"+111 (202) 555-0125\"")
    private String phoneNumber;


    public Customer toCustomer() {
        return Customer.builder()
                .customerId(this.customerId)
                .tc(this.tc)
                .name(this.name)
                .surname(this.surname)
                .phoneNumber(this.phoneNumber)
                .build();
    }

}
