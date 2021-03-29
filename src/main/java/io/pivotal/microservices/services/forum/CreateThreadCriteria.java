package io.pivotal.microservices.services.forum;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

public class CreateThreadCriteria {
    private String accountNumber;

    private String subject;
    private String body;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isValid() {
        if (StringUtils.hasText(accountNumber)) {
            // check if account number is valid here.
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean validate(Errors errors) {
        if (StringUtils.hasText(accountNumber)) {
            if (accountNumber.length() != 9)
                errors.rejectValue("accountNumber", "badFormat",
                        "Account number should be 9 digits");
            else {
                try {
                    Integer.parseInt(accountNumber);
                } catch (NumberFormatException e) {
                    errors.rejectValue("accountNumber", "badFormat",
                            "Account number should be 9 digits");
                }

                //TODO  add validations to check if account exists here.
            }

        }
        else {
            errors.rejectValue("accountNumber", "nonEmpty",
                    "Must specify an account number");

        }

        return errors.hasErrors();
    }

    @Override
    public String toString() {

        return ("accountNumner : " + accountNumber +
                " subject : " + subject +
                " body : " + body);
    }
}