package org.example.mapper;

import org.example.dto.OrderReadDto;
import org.example.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderReadMapper implements Mapper<Order, OrderReadDto> {

    @Override
    public OrderReadDto map(Order entity) {
        return OrderReadDto.builder()
                .number(entity.getNumber())
                .sum(entity.getSum())
                .creationDate(entity.getCreationDate())
                .customerEmail(entity.getCustomerEmail())
                .emailSent(entity.getEmailSent())
                .build();
    }
}
