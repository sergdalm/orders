package org.example.service;

public interface MailService {

    boolean sendEmail(String customerEmail, String orderNumber);
}
