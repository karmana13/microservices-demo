package io.pivotal.microservices.services.forum;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Post DTO - used to interact with the {@link ForumPostsService}.
 *
 * @author Karmana Trivedi
 */
@JsonRootName("Post")
public class Post {

    protected Long id;
    protected String number;
    protected String thread;
    protected String subject;
    protected String body;

    /**
     * default constructor. does nothing.
     */
    protected Post() { }

    /**
     * Constructor that creates a Post object with below parameters.
     * @param accountNumber
     * @param subject
     * @param body
     */
    protected Post(String accountNumber, String subject, String body ) {
        this.number = accountNumber;
        this.subject = subject;
        this.body = body;
    }

    /**
     * Get and Set methods.
     */

    public long getId() {
        return id;
    }
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
        return "[" + thread + "]" + number + " [" + subject + "]: $" + body;
    }

}