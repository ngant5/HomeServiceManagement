package net.codejava.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration 
@EnableScheduling  
@ComponentScan(basePackages = "net.codejava")  
public class AppConfig {
}

