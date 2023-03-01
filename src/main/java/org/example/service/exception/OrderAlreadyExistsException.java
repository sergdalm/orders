package org.example.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception is thrown when attempting to create already exiting order (with the same order number).
 */
@AllArgsConstructor
@Getter
public class OrderAlreadyExistsException extends RuntimeException {

    String orderNumber;
}
