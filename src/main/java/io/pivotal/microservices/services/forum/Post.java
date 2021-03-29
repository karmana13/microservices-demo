package io.pivotal.microservices.services.forum;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Account DTO - used to interact with the {@link ForumAcccountsService}.
 *
 * @author Paul Chapman
 */
@JsonRootName("Post")
public class Post {

    protected Long id;
    protected String number;
    protected String thread;
    protected String subject;
    protected String body;

    /**
     * Default constructor for JPA only.
     */
    protected Post() { }

    public long getId() {
        return id;
    }

    /**
     * Set JPA id - for testing and JPA only. Not intended for normal use.
     *
     * @param id
     *            The new id.
     */
    protected void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    protected void setNumber(String accountNumber) {
        this.number = accountNumber;
    }

    public String getThread() {
        return thread;
    }

    protected void setThread(String thread) {
        this.thread = thread;
    }

    public String getSubject() {
        return subject;
    }

    protected void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    protected void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return number + " [" + subject + "]: $" + body;
    }

}