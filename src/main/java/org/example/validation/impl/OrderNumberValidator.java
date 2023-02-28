package org.example.validation.impl;

import org.example.validation.OrderNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validate the order number to be in format year-month-day-order_number
 */
public class OrderNumberValidator implements ConstraintValidator<OrderNumber, CharSequence> {

    private final String orderNumberRegex = "^(?<date>d{4}-d{2}-d{2})-d+";
    private final Pattern orderNumberRegexPattern = Pattern.compile(orderNumberRegex);
    private final String dateGroupName = "date";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public boolean isValid(CharSequence orderNumber, ConstraintValidatorContext context) {
        Matcher matcher = orderNumberRegexPattern.matcher(orderNumber);
        dateFormat.setLenient(false);
        try {
            String dateAsString = matcher.group(dateGroupName);
            dateFormat.parse(dateAsString);
        } catch (ParseException | IllegalStateException exception) {
            return false;
        }
        return matcher.matches();
    }
}
