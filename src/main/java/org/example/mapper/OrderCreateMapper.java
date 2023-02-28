package org.example.mapper;

import org.example.dto.OrderCreateDto;
import org.example.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateMapper implements Mapper<OrderCreateDto, Order> {

    @Override
    public Order map(OrderCreateDto dto) {
        return Order.builder()
                .number(dto.getOrderNumber())
                .sum(dto.getSum())
                .creationDate(dto.getCreationDate())
                .customerEmail(dto.getCustomerEmail())
                .build();
    }
}
