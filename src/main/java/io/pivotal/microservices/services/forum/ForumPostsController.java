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

import java.util.List;
import java.util.logging.Logger;

/**
 * Client controller, fetches Account info from the microservice via
 * {@link ForumAcccountsService}.
 *
 * @author Paul Chapman
 */
@Controller
public class ForumPostsController {

    protected String currentThread;

    @Autowired
    protected ForumPostsService postsService;

    protected Logger logger = Logger.getLogger(ForumPostsController.class.getName());

    public ForumPostsController(ForumPostsService postsService) {
        this.postsService = postsService;
    }

    @InitBinder
    // TODO need to understand purpose of this.
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("accountNumber", "subject", "body");
    }

    @RequestMapping("/posts")
    public String goHome() {
        return "index";
    }

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
    @RequestMapping("/posts/getthreads")
    public String getThreads(Model model) {
        logger.info("forum-service getThreads() invoked.");

        List<String> threads = postsService.getThreads();
        logger.info("forum-service getThreads() found: " + threads);
        //model.addAttribute("search", name);
        if (threads != null)
            model.addAttribute("threads", threads);
        return "threads";
    }

    @RequestMapping("/posts/subject/{subject}")
    public String bySubject(Model model, @PathVariable("subject") String subject) {
        logger.info("forum-service bySubject() invoked: " + subject);

        List<Post> posts = postsService.bySubjectContains(subject);
        logger.info("forum-service bySubject() found: " + posts);
        //model.addAttribute("search", name);
        if (posts != null)
            model.addAttribute("posts", posts);
        return "posts";
    }
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
    @RequestMapping("/posts/getforum")
    public String getForum(Model model) {
        logger.info("forum-service getForum() invoked.");

        List<Post> posts = postsService.getForum();
        //model.addAttribute("search", name);
        if (posts != null)
            model.addAttribute("posts", posts);
        return "posts";
    }

    @RequestMapping(value = "/posts/createthread", method = RequestMethod.GET)
    public String createThreadForm(Model model) {
        model.addAttribute("createThreadCriteria", new CreateThreadCriteria());
        currentThread = null;
        logger.info("currentThread = " + currentThread);
        return "createthread";
    }

    @RequestMapping(value = "/posts/thread/{thread}/addtothread", method = RequestMethod.GET)
    public String addToThreadForm(Model model, @PathVariable("thread") String thread) {
        logger.info("adding post to thread" + thread);
        currentThread = thread;
        model.addAttribute("createThreadCriteria", new CreateThreadCriteria());
        return "createthread";
    }

    @RequestMapping(value = "/posts/docreatethread")
    public String doCreateThread(Model model, CreateThreadCriteria criteria, BindingResult result) {
        logger.info("post-service doCreateThread() invoked: " + criteria);

        criteria.validate(result);

        if (result.hasErrors())
            return "createthread";

        logger.info("criteria = " + criteria);
        String accountNumber = criteria.getAccountNumber();
        String subject = criteria.getSubject();
        String body = criteria.getBody();

        logger.info("currentThread = " + currentThread);
        if(null != currentThread) {
            String thread = currentThread;
            postsService.addToThread(thread, accountNumber, subject, body);
        }
        else
        {
            postsService.createThread(accountNumber, subject, body);
        }

        // postsService.createThread(accountNumber, subject, body);
        //return byNumber(model, accountNumber);
        return getForum(model);
    }
}