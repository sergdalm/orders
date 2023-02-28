package org.example.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class OrderReadDto {

    String number;
    BigDecimal sum;
    LocalDate creationDate;
    String customerEmail;
    Boolean emailSent;
}
