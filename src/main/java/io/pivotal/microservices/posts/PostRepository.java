package io.pivotal.microservices.posts;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * Repository for Account data implemented using Spring Data JPA.
 *
 * @author Paul Chapman
 */
public interface PostRepository extends Repository<Post, Long> {
    /**
     * Find an account with the specified account number.
     *
     * @param accountNumber
     * @return The account if found, null otherwise.
     */
    public Post findByNumber(String accountNumber);
    public List<Post> findByThread(String thread);

    /**
     * Find accounts whose owner name contains the specified string
     *
     * @param partialName
     *            Any alphabetic string.
     * @return The list of matching accounts - always non-null, but may be
     *         empty.
     */
    public List<Post> findBySubjectContainingIgnoreCase(String partialName);

    /**
     * Fetch the number of accounts known to the system.
     *
     * @return The number of accounts.
     */
    @Query("SELECT count(*) from Post")
    public int countAccounts();
}