package org.example.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception is thrown when the message cannot be sent.
 */
@AllArgsConstructor
@Getter
public class SendFailedException extends RuntimeException {

    private final String customerEmail;
    private final String orderNumber;
}
