package org.example.service.impl;

import lombok.AllArgsConstructor;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderReadDto;
import org.example.entity.Order;
import org.example.mapper.OrderCreateMapper;
import org.example.mapper.OrderReadMapper;
import org.example.repository.OrderRepository;
import org.example.service.MailService;
import org.example.service.OrderService;
import org.example.service.exception.SendFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MailService mailService;
    private final OrderCreateMapper orderCreateMapper;
    private final OrderReadMapper orderReadMapper;

    @Override
    public List<OrderReadDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderReadMapper::map)
                .toList();
    }

    @Override
    public List<OrderReadDto> getAllOrdersWithNotSentNotification() {
        return orderRepository.findAllOrdersWithNotSentEmails().stream()
                .map(orderReadMapper::map)
                .toList();
    }

    @Override
    public Optional<OrderReadDto> getOrder(String orderNumber) {
        return orderRepository.findById(orderNumber)
                .map(orderReadMapper::map);
    }

    @Transactional(noRollbackFor = {SendFailedException.class})
    @Override
    public Optional<OrderReadDto> create(OrderCreateDto orderCreateDto) {
        return Optional.of(orderCreateDto)
                .map(newOrderDto -> {
                    Order newOrder = orderCreateMapper.map(newOrderDto);
                    boolean isEmailSent = mailService.sendEmail(newOrder.getCustomerEmail(), newOrder.getNumber());
                    newOrder.setEmailSent(isEmailSent);
                    orderRepository.save(newOrder);
                    if (!isEmailSent) {
                        throw new SendFailedException(newOrder.getCustomerEmail(), newOrder.getNumber());
                    }
                    return newOrder;
                })
                .map(orderReadMapper::map);
    }

    @Transactional
    @Override
    public Optional<OrderReadDto> updateCustomerEmailAndResendNotification(String orderNumber, String newEmail) {
        return orderRepository.findById(orderNumber)
                .map(order -> {
                    if (!order.getCustomerEmail().equals(newEmail)) {
                        boolean isEmailSent = mailService.sendEmail(newEmail, orderNumber);
                        order.setEmailSent(isEmailSent);
                        orderRepository.save(order);
                        if (!isEmailSent) {
                            throw new SendFailedException(newEmail, orderNumber);
                        }
                    }
                    return order;
                })
                .map(orderReadMapper::map);
    }

    @Override
    public boolean deleteOrder(String orderNumber) {
        return orderRepository.findById(orderNumber)
                .map(order -> {
                    orderRepository.delete(order);
                    orderRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
