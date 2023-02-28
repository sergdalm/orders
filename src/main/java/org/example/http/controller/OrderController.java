package org.example.http.controller;

import lombok.AllArgsConstructor;
import org.example.dto.OrderCreateDto;
import org.example.dto.OrderReadDto;
import org.example.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Email;
import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Get all orders
     *
     * @return all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderReadDto>> getAllOrder() {
        List<OrderReadDto> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Get orders on which customers were not sent notifications
     *
     * @return orders
     */
    @GetMapping("/notSent")
    public ResponseEntity<List<OrderReadDto>> getNotSentOrders() {
        List<OrderReadDto> orders = orderService.getAllOrdersWithNotSentNotification();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Get specified order
     *
     * @param orderNumber as year-month-day-order_number
     * @return order
     */
    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderReadDto> getOrder(@PathVariable("orderNumber") String orderNumber) {
        OrderReadDto order = orderService.getOrder(orderNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Create new order
     *
     * @param orderCreateDto
     * @return order
     */
    @PostMapping
    public ResponseEntity<OrderReadDto> createOrder(@Validated OrderCreateDto orderCreateDto) {
        OrderReadDto order = orderService.create(orderCreateDto).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    /**
     * Update customer email and resend notification on specified order
     *
     * @param orderNumber
     * @param email
     * @return updated order
     */
    @PutMapping("/{orderNumber}")
    public ResponseEntity<OrderReadDto> updateEmailAndResendNotification(@PathVariable("orderNumber") String orderNumber,
                                                                         @Email String email) {
        OrderReadDto order = orderService.updateCustomerEmailAndResendNotification(orderNumber, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Delete specified order
     *
     * @param orderNumber
     */
    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderNumber") String orderNumber) {
        return orderService.deleteOrder(orderNumber)
                ? noContent().build()
                : notFound().build();
    }
}
