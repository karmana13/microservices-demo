package io.pivotal.microservices.posts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

/**
 * The accounts Spring configuration.
 *
 * @author Paul Chapman
 */
@Configuration
@ComponentScan
@EntityScan("io.pivotal.microservices.posts")
@EnableJpaRepositories("io.pivotal.microservices.posts")
@PropertySource("classpath:db-config.properties")
public class PostsConfiguration {

    protected Logger logger;

    public PostsConfiguration() {
        logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Creates an in-memory "rewards" database populated with test data for fast
     * testing
     */
    @Bean
    public DataSource dataSource() {
        logger.info("dataSource() invoked");

        // Create an in-memory H2 relational database containing some demo
        // accounts.
        DataSource dataSource = (new EmbeddedDatabaseBuilder()).addScript("classpath:testdb2/schema.sql")
                .addScript("classpath:testdb2/data.sql").build();

        logger.info("dataSource = " + dataSource);

        // Sanity check
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> posts = jdbcTemplate.queryForList("SELECT number FROM T_POST");
        logger.info("System has " + posts.size() + " accounts");

        // Populate with random balances
        Random rand = new Random();

        for (Map<String, Object> item : posts) {
            String number = (String) item.get("number");
            //TODO for thread here may be.
            //BigDecimal balance = new BigDecimal(rand.nextInt(10000000) / 100.0).setScale(2, RoundingMode.HALF_UP);
            //jdbcTemplate.update("UPDATE T_POST SET balance = ? WHERE number = ?", balance, number);
        }

        return dataSource;
    }
}