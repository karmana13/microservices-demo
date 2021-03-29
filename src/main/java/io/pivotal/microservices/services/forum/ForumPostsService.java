package io.pivotal.microservices.services.forum;

import io.pivotal.microservices.exceptions.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Hide the access to the microservice inside this local service.
 *
 * @author Paul Chapman
 */
@Service
public class ForumPostsService {

    @Autowired
    @LoadBalanced
    protected RestTemplate restTemplate;

    protected String serviceUrl;

    protected Logger logger = Logger.getLogger(ForumPostsService.class.getName());

    public ForumPostsService(String serviceUrl) {
        this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl;
    }

    /**
     * The RestTemplate works because it uses a custom request-factory that uses
     * Ribbon to look-up the service to use. This method simply exists to show this.
     */
    @PostConstruct
    public void demoOnly() {
        // Can't do this in the constructor because the RestTemplate injection
        // happens afterwards.
        logger.warning("The RestTemplate request factory is " + restTemplate.getRequestFactory().getClass());
    }

    public List<Post> findByNumber(String accountNumber) {
        logger.info("getByNumber() invoked:  for " + accountNumber);
        Post[] posts = null;

        try {
            posts = restTemplate.getForObject(serviceUrl + "/posts/{number}", Post[].class, accountNumber);
        } catch (HttpClientErrorException e) { // 404
            // Nothing found
        }

        if (posts == null || posts.length == 0)
            return null;
        else
            return Arrays.asList(posts);
    }

    public List<Post> bySubjectContains(String name) {
        logger.info("bySubjectContains() invoked:  for " + name);
        Post[] posts = null;

        try {
            posts = restTemplate.getForObject(serviceUrl + "/posts/subject/{name}", Post[].class, name);
        } catch (HttpClientErrorException e) { // 404
            // Nothing found
        }

        if (posts == null || posts.length == 0)
            return null;
        else
            return Arrays.asList(posts);
    }

    public List<Post> byThread(String thread) {
        logger.info("byThread() invoked:  for " + thread);
        Post[] posts = null;

        try {
            posts = restTemplate.getForObject(serviceUrl + "/posts/thread/{thread}", Post[].class, thread);
        } catch (HttpClientErrorException e) { // 404
            // Nothing found
        }

        if (posts == null || posts.length == 0)
            return null;
        else
            return Arrays.asList(posts);
    }

    public List<String> getThreads() {
        logger.info("getThreads() invoked.");
        String[] threads = null;

        try {
            threads = restTemplate.getForObject(serviceUrl + "/posts/getthreads", String[].class);
        } catch (HttpClientErrorException e) { // 404
            // Nothing found
        }

        if (threads == null || threads.length == 0)
            return null;
        else
            return Arrays.asList(threads);
    }

    public void createThread(String accountNumber, String subject, String body) {
        logger.info("createThread() invoked.");

        try {
            restTemplate.getForObject(serviceUrl + "/posts/createthread/{accountNumber}/{subject}/{body}",
                    Post.class, accountNumber, subject, body);
        } catch (Exception e) {
            logger.severe(e.getClass() + ": " + e.getLocalizedMessage());
        }
    }
    public void addToThread(String thread, String accountNumber, String subject, String body) {
        logger.info("createThread() invoked.");

        try {
            restTemplate.getForObject(serviceUrl + "/posts/addtothread/{thread}/{account}/{subject}/{body}",
                    Post.class, thread, accountNumber, subject, body);
        } catch (Exception e) {
            logger.severe(e.getClass() + ": " + e.getLocalizedMessage());
        }
    }




}