package io.pivotal.microservices.posts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistent account entity with JPA markup. Posts are stored in an H2
 * relational database.
 *
 * @author Karmana Trivedi
 */
@Entity
@Table(name = "T_POST")
public class Post implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Long nextId = 1000L;
	public static Long nextThread = 1000L;

	@Id
	protected Long id;

	protected String number;								// Account Number
	protected String thread;								// Thread Number

	@Column(name = "subject")
	protected String subject;								// subject of the post
	protected String body;									// body of the post


	// generate unique IDs.
	protected static Long getNextId() {
		synchronized (nextId) {
			return nextId++;
		}
	}

	// generate unique threadID when creating new thrads.
	protected static Long getNextThread() {
		synchronized (nextThread) {
			return nextThread++;
		}
	}

	// Default Constructor
	protected Post() {
	}

	// Constructor to create a new post object with a unique thread id.
	public Post(String number, String subject, String body) {
		id = getNextId();
		this.number = number;
		this.thread = getNextThread().toString();
		this.subject = subject;
		this.body = body;
	}

	// Constructor to create a new post object with an existing threadid.
	// CAUTION: should confirm that the thread exists before using this.
	public Post(String number, String thread, String subject, String body) {
		id = getNextId();
		this.number = number;
		this.thread = thread;
		this.subject = subject;
		this.body = body;
	}

	// get set apis
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