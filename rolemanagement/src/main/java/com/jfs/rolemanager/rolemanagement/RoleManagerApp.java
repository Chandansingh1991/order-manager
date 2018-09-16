package com.jfs.rolemanager.rolemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.WebApplicationInitializer;

/**
 * @author Chandan Singh Karki
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.jfs.rolemanager"})
public class RoleManagerApp extends SpringBootServletInitializer implements WebApplicationInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder springApplicationBuilder) {
        return springApplicationBuilder.sources(RoleManagerApp.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(RoleManagerApp.class, args);

    }
}
