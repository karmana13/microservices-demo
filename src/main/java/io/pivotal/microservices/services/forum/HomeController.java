package io.pivotal.microservices.services.forum;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home page controller.
 *
 * @author Karmana Trivedi
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "index";
    }

}