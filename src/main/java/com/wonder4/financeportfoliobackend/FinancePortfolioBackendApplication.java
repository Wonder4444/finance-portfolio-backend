package com.wonder4.financeportfoliobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinancePortfolioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancePortfolioBackendApplication.class, args);
    }
}
