package me.brilliant.system;

import lombok.extern.slf4j.Slf4j;
import me.brilliant.boot.web.utils.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/9
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableWebSecurity
@EnableFeignClients
@ComponentScan(basePackages = {"me.brilliant.system", "me.brilliant.boot.web"})
@Slf4j
public class BrilliantApplication {

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication.run(BrilliantApplication.class, args).getEnvironment();

        log.info("\n----------------------------------------------------------\n\t" +
                        "Application: '{}' is running Success! \n\t" +
                        "Local URL: \thttp://localhost:{}\n\t" +
                        "Document:\thttp://localhost:{}/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                env.getProperty("server.port"));
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

}
