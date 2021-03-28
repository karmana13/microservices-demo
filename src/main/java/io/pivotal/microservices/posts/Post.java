package io.pivotal.microservices.posts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Persistent account entity with JPA markup. Accounts are stored in an H2
 * relational database.
 *
 * @author Paul Chapman
 */
@Entity
@Table(name = "T_POST")
public class Post implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Long nextId = 0L;

	@Id
	protected Long id;

	protected String number;
	protected String thread;

	@Column(name = "subject")
	protected String subject;
	protected String body;


	/**
	 * This is a very simple, and non-scalable solution to generating unique
	 * ids. Not recommended for a real application. Consider using the
	 * <tt>@GeneratedValue</tt> annotation and a sequence to generate ids.
	 *
	 * @return The next available id.
	 */
	protected static Long getNextId() {
		synchronized (nextId) {
			return nextId++;
		}
	}

	/**
	 * Default constructor for JPA only.
	 */

	protected Post() {

	}
	public Post(String number, String subject) {
		id = getNextId();
		this.number = number;
		this.thread = "777777777";
		this.subject = subject;
		this.body = "empty body";

	}

	public Post(String number, String thread, String subject, String body) {
		id = getNextId();
		this.number = number;
		this.thread = thread;
		this.subject = subject;
		this.body = body;
	}

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