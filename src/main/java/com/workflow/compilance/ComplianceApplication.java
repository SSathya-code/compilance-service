package com.workflow.compilance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ComplianceApplication {
    static void main(String[] args) {
        log.info("Starting Compliance Application");
        SpringApplication.run(ComplianceApplication.class, args);
    }
}
