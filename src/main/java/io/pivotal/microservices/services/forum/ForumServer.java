package io.pivotal.microservices.services.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import io.pivotal.microservices.services.registration.RegistrationServer;

/**
 * Forum-server. Works as a microservice client, fetching data from the
 * Account-Service and Post-Service.
 * Uses the Discovery Server (Eureka) to find the microservice.
 *
 * @author Karmana Trivedi
 */
@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class, //
        DataSourceAutoConfiguration.class })
@EnableDiscoveryClient
@ComponentScan(useDefaultFilters = false) // Disable component scanner
public class ForumServer {

    /**
     * URL uses the logical name of account-service - upper or lower case, doesn't
     * matter.
     */
    public static final String ACCOUNTS_SERVICE_URL = "http://ACCOUNTS-SERVICE";
    public static final String POSTS_SERVICE_URL = "http://POSTS-SERVICE";

    /**
     * Run the application using Spring Boot and an embedded servlet engine.
     *
     * @param args Program arguments - ignored.
     */
    public static void main(String[] args) {
        // Default to registration server on localhost
        if (System.getProperty(RegistrationServer.REGISTRATION_SERVER_HOSTNAME) == null)
            System.setProperty(RegistrationServer.REGISTRATION_SERVER_HOSTNAME, "localhost");

        // Tell server to look for web-server.properties or web-server.yml
        System.setProperty("spring.config.name", "forum-server");
        SpringApplication.run(ForumServer.class, args);
    }

    /**
     * A customized RestTemplate that has the ribbon load balancer build in. Note
     * that prior to the "Brixton"
     *
     * @return
     */
    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * The AccountService encapsulates the interaction with the micro-service.
     *
     * @return A new service instance.
     */
    @Bean
    public ForumAccountsService accountsService() {
        return new ForumAccountsService(ACCOUNTS_SERVICE_URL);
    }

    /**
     * Create the controller, passing it the {@link ForumAccountsService} to use.
     *
     * @return
     */
    @Bean
    public ForumAccountsController accountsController() {
        return new ForumAccountsController(accountsService());
    }

    /**
     * The PostService encapsulates the interaction with the micro-service.
     *
     * @return A new service instance.
     */
    @Bean
    public ForumPostsService postsService() {
        return new ForumPostsService(POSTS_SERVICE_URL);
    }

    /**
     * Create the controller, passing it the {@link ForumPostsService} to use.
     *
     * @return
     */
    @Bean
    public ForumPostsController postsController() {
        return new ForumPostsController(postsService());
    }

    @Bean
    public HomeController homeController() {
        return new HomeController();
    }
}