package io.pivotal.microservices.posts;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for Post data implemented using Spring Data JPA.
 *
 * @author Karmana Trivedi
 */
public interface PostRepository extends CrudRepository<Post, Long> {
    /**
     * Find a post by specified account.
     *
     * @param accountNumber
     * @return The account if found, null otherwise.
     */
    public List<Post> findByNumber(String accountNumber);

    /**
     * Find posts whose thread is the specified string
     *
     * @param thread
     *            a valid thread
     * @return The list of matching posts - always non-null, but may be
     *         empty.
     */
    public List<Post> findByThread(String thread);

    /**
     * Fetch the number of posts known to the system.
     *
     * @return The number of posts.
     */
    @Query("SELECT count(*) from Post")
    public int countPosts();

    /**
     * Fetch the threads known to the system.
     *
     * @return The list of threads in string format.
     */
    @Query("SELECT DISTINCT thread from Post")
    public List<String> getDistinctThreads();


}