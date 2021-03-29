package io.pivotal.microservices.services.forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Client controller, fetches Post info from the microservice via
 * {@link ForumPostsService}.
 *
 * @author Karmana Trivedi
 */
@Controller
public class ForumPostsController {

    protected String currentThread;
    protected String currentAccountNumber;

    @Autowired
    protected ForumPostsService postsService;

    protected Logger logger = Logger.getLogger(ForumPostsController.class.getName());

    public ForumPostsController(ForumPostsService postsService) {
        this.postsService = postsService;
    }

    @InitBinder

    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("accountNumber", "subject", "body");
    }

    @RequestMapping("/posts")
    public String goHome() {
        return "index";
    }

    /**
     * Service returms all the posts by a particular account.
     * @param model
     * @param accountNumber
     * @return
     */
    @RequestMapping("/posts/account/{accountNumber}")
    public String byNumber(Model model, @PathVariable("accountNumber") String accountNumber) {

        logger.info("post-service byNumber() invoked: " + accountNumber);

        List<Post> posts = postsService.findByNumber(accountNumber);
        logger.info("post-service byNumber() found: " + posts);

        if (posts == null) { // no such account
            model.addAttribute("number", accountNumber);
            return "posts";
        }

        model.addAttribute("posts", posts);
        return "posts";
    }

    /**
     * Service returns all the posts part of a particular thread.
     * @param model
     * @param thread
     * @return
     */
    @RequestMapping("/posts/thread/{thread}")
    public String byThread(Model model, @PathVariable("thread") String thread) {
        logger.info("forum-service byThread() invoked: " + thread);

        List<Post> posts = postsService.byThread(thread);
        logger.info("forum-service byThread() found: " + posts);
        //model.addAttribute("search", name);
        if (posts != null)
            model.addAttribute("posts", posts);
        return "posts";
    }

    /**
     * Called to access the forum(List of Threads) after User Login.
     * saves the account number current user to be used in saving post.
     * @param model
     * @param accountNumber
     * @return
     */
    @RequestMapping("/accounts/dologin/{accountNumber}/posts/getthreads")
    public String getThreads(Model model, @PathVariable("accountNumber") String accountNumber) {
        logger.info("forum-service getThreads() invoked.");

        currentAccountNumber = accountNumber;
        logger.info("forum-service currentAccountNumber = " + currentAccountNumber);

        List<String> threads = postsService.getThreads();
        logger.info("forum-service getThreads() found: " + threads);

        List<Post> firstPosts = new ArrayList<Post>();
        for(String thread : threads)
        {
            List<Post> posts = postsService.byThread(thread);
            firstPosts.add(posts.get(0));
        }

        //model.addAttribute("search", name);
        if (threads != null)
            model.addAttribute("firstposts", firstPosts);
        return "threads";
    }

    /**
     * Service calls createthread form to create a new thread.
     * @param model
     * @return
     */
    @RequestMapping(value = "/posts/createthread", method = RequestMethod.GET)
    public String createThreadForm(Model model) {
        model.addAttribute("createThreadCriteria", new CreateThreadCriteria());
        currentThread = null;
        logger.info("currentThread = " + currentThread);
        return "createthread";
    }

    /**
     * Service calls createthread form to create a new post in existing thread.
     * @param model
     * @param thread
     * @return
     */
    @RequestMapping(value = "/posts/thread/{thread}/addtothread", method = RequestMethod.GET)
    public String addToThreadForm(Model model, @PathVariable("thread") String thread) {
        logger.info("adding post to thread" + thread);
        currentThread = thread;
        model.addAttribute("createThreadCriteria", new CreateThreadCriteria());
        return "createthread";
    }

    /**
     * Service that is executed when User submits CreateThread form. it validates and add a new post based on
     * @param model
     * @param criteria
     * @param result
     * @return
     */
    @RequestMapping(value = "/posts/docreatethread")
    public String doCreateThread(Model model, CreateThreadCriteria criteria, BindingResult result) {
        logger.info("post-service doCreateThread() invoked: " + criteria);
        criteria.validate(result);

        if (result.hasErrors())
            return "createthread";

        logger.info("criteria = " + criteria);
        //String accountNumber = criteria.getAccountNumber();
        String accountNumber = currentAccountNumber;
        String subject = criteria.getSubject();
        String body = criteria.getBody();
        logger.info("currentThread = " + currentThread);
        String thread = currentThread;
        if(null != currentThread) {
            postsService.addToThread(thread, accountNumber, subject, body);
        }
        else
        {
            postsService.createThread(accountNumber, subject, body);
        }

        return byNumber(model, accountNumber);
    }

}