package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "number")
@Table(name = "orders")
public class Order {

    @Id
    private String number;

    @Column(nullable = false)
    private BigDecimal sum;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private Boolean emailSent;
}
