package org.example.service;

import org.example.dto.OrderCreateDto;
import org.example.dto.OrderReadDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderReadDto> getAllOrders();

    List<OrderReadDto> getAllOrdersWithNotSentNotification();

    Optional<OrderReadDto> getOrder(String orderNumber);

    Optional<OrderReadDto> create(OrderCreateDto orderCreateDto);

    Optional<OrderReadDto> updateCustomerEmailAndResendNotification(String orderNumber, String newEmail);

    boolean deleteOrder(String orderNumber);
}
