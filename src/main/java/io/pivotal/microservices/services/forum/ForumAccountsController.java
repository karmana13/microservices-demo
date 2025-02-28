package io.pivotal.microservices.services.forum;

import java.util.List;
import java.util.logging.Logger;

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

/**
 * Client controller, fetches Account info from the microservice via
 * {@link ForumAccountsService}.
 *
 * @author Karmana Trivedi
 */
@Controller
public class ForumAccountsController {

    @Autowired
    protected ForumAccountsService accountsService;

    protected Logger logger = Logger.getLogger(ForumAccountsController.class.getName());

    public ForumAccountsController(ForumAccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields("accountNumber", "searchText");
    }

    @RequestMapping("/accounts")
    public String goHome() {
        return "index";
    }

    @RequestMapping("/accounts/{accountNumber}")
    public String byNumber(Model model, @PathVariable("accountNumber") String accountNumber) {

        logger.info("forum-service byNumber() invoked: " + accountNumber);

        Account account = accountsService.findByNumber(accountNumber);

        if (account == null) { // no such account
            model.addAttribute("number", accountNumber);
            return "account";
        }

        logger.info("forum-service byNumber() found: " + account);
        model.addAttribute("account", account);
        return "account";
    }

    @RequestMapping("/accounts/owner/{text}")
    public String ownerSearch(Model model, @PathVariable("text") String name) {
        logger.info("forum-service byOwner() invoked: " + name);

        List<Account> accounts = accountsService.byOwnerContains(name);
        logger.info("forum-service byOwner() found: " + accounts);
        model.addAttribute("search", name);
        if (accounts != null)
            model.addAttribute("accounts", accounts);
        return "accounts";
    }

    @RequestMapping(value = "/accounts/search", method = RequestMethod.GET)
    public String searchForm(Model model) {
        model.addAttribute("searchCriteria", new SearchCriteria());
        return "accountSearch";
    }

    @RequestMapping(value = "/accounts/dosearch")
    public String doSearch(Model model, SearchCriteria criteria, BindingResult result) {
        logger.info("forum-service search() invoked: " + criteria);

        criteria.validate(result);

        if (result.hasErrors())
            return "accountSearch";

        String accountNumber = criteria.getAccountNumber();
        if (StringUtils.hasText(accountNumber)) {
            return byNumber(model, accountNumber);
        } else {
            String searchText = criteria.getSearchText();
            return ownerSearch(model, searchText);
        }
    }

    @RequestMapping(value = "/accounts/login", method = RequestMethod.GET)
    public String loginForm(Model model) {
        model.addAttribute("searchCriteria", new SearchCriteria());
        return "accountLogin";
    }

    @RequestMapping(value = "/accounts/dologin")
    public String doLogin(Model model, SearchCriteria criteria, BindingResult result) {
        logger.info("forum-service login() invoked: " + criteria);

        criteria.validate(result);

        if (result.hasErrors())
            return "accountLogin";

        String accountNumber = criteria.getAccountNumber();
        if (StringUtils.hasText(accountNumber)) {
            return byNumber(model, accountNumber);
        }
        else
        {
            return "accountLogin";
        }
    }

}