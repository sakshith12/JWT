package com.bfh.qualifier;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Collections;
import com.fasterxml.jackson.annotation.JsonProperty;

@SpringBootApplication
public class BajajFinservHealthApplication implements CommandLineRunner {

    private final SubmissionService submissionService;

    public BajajFinservHealthApplication(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BajajFinservHealthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        submissionService.processAndSubmit();
    }
}
