package io.pivotal.microservices.posts;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.microservices.exceptions.PostNotFoundException;
import io.pivotal.microservices.exceptions.ThreadNotFoundException;

/**
 * A RESTFul controller for accessing account information.
 *
 * @author Paul Chapman
 */
@RestController
public class PostsController {

    protected Logger logger = Logger.getLogger(PostsController.class
            .getName());
    protected PostRepository postRepository;

    /**
     * Create an instance plugging in the respository of Posts.
     *
     * @param postRepository
     *            An post repository implementation.
     */
    @Autowired
    public PostsController(PostRepository postRepository) {
        this.postRepository = postRepository;

        logger.info("PostRepository says system has "
                + postRepository.countAccounts() + " accounts");
    }

    /**
     * Fetch an account with the specified account number.
     *
     * @param accountNumber
     *            A numeric, 9 digit account number.
     * @return The account if found.
     * @throws PostNotFoundException
     *             If the number is not recognised.
     */
    @RequestMapping("/posts/{accountNumber}")
    public Post byNumber(@PathVariable("accountNumber") String accountNumber) {

        logger.info("accounts-service byNumber() invoked: " + accountNumber);
        Post account = postRepository.findByNumber(accountNumber);
        logger.info("accounts-service byNumber() found: " + account);

        if (account == null)
            throw new PostNotFoundException(accountNumber);
        else {
            return account;
        }
    }

    /**
     * Fetch posts with the specified thread. A partial case-insensitive match
     * is supported. So <code>http://.../posts/thread/a</code> will find any
     * accounts with upper or lower case 'a' in their name.
     *
     * @param thread
     * @return A non-null, non-empty set of posts.
     * @throws ThreadNotFoundException
     *             If there are no matches at all.
     */
    @RequestMapping("/posts/thread/{thread}")
    public List<Post> byThread(@PathVariable("thread") String thread) {
        logger.info("accounts-service byThread() invoked: "
                + postRepository.getClass().getName() + " for "
                + thread);

        List<Post> posts = postRepository
                .findByThread(thread);
        logger.info("accounts-service byThread() found: " + posts);

        if (posts == null || posts.size() == 0)
            throw new ThreadNotFoundException(thread);
        else {
            return posts;
        }
    }


    /**
     * Fetch accounts with the specified name. A partial case-insensitive match
     * is supported. So <code>http://.../accounts/owner/a</code> will find any
     * accounts with upper or lower case 'a' in their name.
     *
     * @param partialName
     * @return A non-null, non-empty set of accounts.
     * @throws PostNotFoundException
     *             If there are no matches at all.
     */
    @RequestMapping("/posts/owner/{name}")
    public List<Post> byOwner(@PathVariable("name") String partialName) {
        logger.info("accounts-service byOwner() invoked: "
                + postRepository.getClass().getName() + " for "
                + partialName);

        List<Post> accounts = postRepository
                .findBySubjectContainingIgnoreCase(partialName);
        logger.info("accounts-service byOwner() found: " + accounts);

        if (accounts == null || accounts.size() == 0)
            throw new PostNotFoundException(partialName);
        else {
            return accounts;
        }
    }
}