package io.pivotal.microservices.posts;
import java.util.ArrayList;
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
     * Fetch a post with the specified account number.
     *
     * @param accountNumber
     *            A numeric, 9 digit account number.
     * @return A non-null, non-empty set of posts
     * @throws PostNotFoundException
     *             If the number is not recognised.
     */
    @RequestMapping("/posts/{accountNumber}")
    public List<Post> byNumber(@PathVariable("accountNumber") String accountNumber) {

        logger.info("posts-service byNumber() invoked: " + accountNumber);
        //Post account = postRepository.findByNumber(accountNumber);
        List<Post> posts = postRepository
                .findByNumber(accountNumber);
        logger.info("posts-service byNumber() found: " + posts);

        if (posts == null)
            throw new PostNotFoundException(accountNumber);
        else {
            return posts;
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
        logger.info("posts-service byThread() invoked: "
                + postRepository.getClass().getName() + " for "
                + thread);

        List<Post> posts = postRepository
                .findByThread(thread);
        logger.info("posts-service byThread() found: " + posts);

        if (posts == null || posts.size() == 0)
            throw new ThreadNotFoundException(thread);
        else {
            return posts;
        }
    }


    /**
     * add post with the specified thread.
     * So <code>http://.../posts/addtothread/{thread}/{account}/{subject}/{body}
     * will add new post
     *
     * @param thread
     * @param account
     * @param subject
     * @param body
     * @return A String
     * @throws ThreadNotFoundException
     *             If there are no matches at all.
     */
    @RequestMapping("/posts/addtothread/{thread}/{account}/{subject}/{body}")
    public String addtothread(@PathVariable("thread") String thread,
                             @PathVariable("account") String account,
                             @PathVariable("subject") String subject,
                             @PathVariable("body") String body) {
        logger.info("posts-service addtothread() invoked: "
                + postRepository.getClass().getName() + " for "
                + "thread: " + thread
                + "account: " + account
                + "subject: " + subject
                + "body: " + body
        );

        List<Post> posts = postRepository.findByThread(thread);
        logger.info("posts-service addtothread() found: " + posts);

        if (posts == null || posts.size() == 0)
            throw new ThreadNotFoundException(thread);
        else {
            logger.info("posts-service addtothread() : thread exists. adding a post" );
            Post document = new Post(account, thread, subject, body); // Note: order is different here.
            postRepository.save(document);
            return "success";
        }
    }


    /**
     * add post by creating new thread.
     * So <code>http://.../posts/createthread/{account}/{subject}/{body}
     * will add new post to a new thread.
     *
     * @param account
     * @param subject
     * @param body
     * @return A String
     * @throws ThreadNotFoundException
     *             If there are no matches at all.
     */
    @RequestMapping("/posts/createthread/{account}/{subject}/{body}")
    public String createThread(@PathVariable("account") String account,
                               @PathVariable("subject") String subject,
                               @PathVariable("body") String body) {
        logger.info("posts-service createThread() invoked: "
                + postRepository.getClass().getName() + " for "
                + "account: " + account
                + "subject: " + subject
                + "body: " + body
        );

        logger.info("posts-service createThread() : adding a post with new thread" );
        Post document = new Post(account, subject, body); // Note: order is different here.
        postRepository.save(document);
        return "success";

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
    @RequestMapping("/posts/subject/{name}")
    public List<Post> bySubject(@PathVariable("name") String partialName) {
        logger.info("accounts-service bySubject() invoked: "
                + postRepository.getClass().getName() + " for "
                + partialName);

        List<Post> posts = postRepository
                .findBySubjectContainingIgnoreCase(partialName);
        logger.info("accounts-service bySubject() found: " + posts);

        if (posts == null || posts.size() == 0)
            throw new PostNotFoundException(partialName);
        else {
            return posts;
        }
    }


    /**
     * Fetch all posts group by threads.
     *
     * @return A non-null, non-empty set of threads, that includes all posts.
     * @throws PostNotFoundException
     *             If there are no matches at all.
     */
    @RequestMapping("/posts/getforum")
    public List<List<Post>> getforum() {
        logger.info("posts-service getforum() invoked: "
                + postRepository.getClass().getName());

        List<List<Post>> results = new ArrayList<List<Post>>();

        List<String> threads = postRepository.getDistinctThreads();

        logger.info("post-service getforum() found threads : " + threads);

        for(String thread: threads)
        {
            List<Post> postsInThread = postRepository.findByThread(thread);
            results.add(postsInThread);
        }

        for(List<Post> plist: results)
        {
            logger.info("post-service getforum() thread by thread:" + plist);
        }

        logger.info("post-service getforum() results :" + results);

        return results;
    }
}