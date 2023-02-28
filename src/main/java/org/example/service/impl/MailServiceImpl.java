package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.service.MailService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    private final String testEmail = "@test-email.com";

    @Override
    public boolean sendEmail(String customerEmail, String orderNumber) {
        if (checkEmail(customerEmail)) {
            log.info("Failed to send notification");
            return false;
        }
        log.info("Notification on order N {} has been successfully sent", orderNumber);
        return true;
    }

    private boolean checkEmail(String email) {
        return email.endsWith(testEmail);
    }
}
