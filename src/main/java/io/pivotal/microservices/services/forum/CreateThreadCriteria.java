package io.pivotal.microservices.services.forum;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

// criteria class to validate add post form.
public class CreateThreadCriteria {

    private String subject;
    private String body;


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
        return true;
    }

    public boolean validate(Errors errors) {
        return false;
    }

    @Override
    public String toString() {

        return (" subject : " + subject +
                " body : " + body);
    }
}