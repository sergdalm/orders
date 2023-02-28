package org.example.repository;

import org.example.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("select o from Order o where o.emailSent = FALSE")
    List<Order> findAllOrdersWithNotSentEmails();
}
