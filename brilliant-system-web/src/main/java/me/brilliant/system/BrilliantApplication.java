package me.brilliant.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
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
public class BrilliantApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrilliantApplication.class, args);
    }

}
