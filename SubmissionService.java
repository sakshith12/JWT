package com.bfh.qualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class SubmissionService {

    private final RestTemplate restTemplate;

    @Autowired
    public SubmissionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void processAndSubmit() {
        try {
            System.out.println("Step 1: Generating webhook...");

            String generateWebhookUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            HttpHeaders generateWebhookHeaders = new HttpHeaders();
            generateWebhookHeaders.setContentType(MediaType.APPLICATION_JSON);
            
            WebhookRequest webhookRequest = new WebhookRequest("John Doe", "REG12347", "john@example.com");
            HttpEntity<WebhookRequest> generateWebhookEntity = new HttpEntity<>(webhookRequest, generateWebhookHeaders);

            ResponseEntity<WebhookResponse> webhookResponse = restTemplate.exchange(
                    generateWebhookUrl,
                    HttpMethod.POST,
                    generateWebhookEntity,
                    WebhookResponse.class
            );

            if (webhookResponse.getStatusCode() != HttpStatus.OK || webhookResponse.getBody() == null) {
                System.err.println("Failed to get a valid response from the webhook generation API.");
                return;
            }

            String webhookUrl = webhookResponse.getBody().getWebhook();
            String accessToken = webhookResponse.getBody().getAccessToken();

            System.out.println("Webhook URL received: " + webhookUrl);
            System.out.println("Access Token received: " + accessToken);

            System.out.println("Step 2: Solving the SQL problem and preparing the final query...");

            String finalQuery = "SELECT\n" +
                                "    e1.EMP_ID,\n" +
                                "    e1.FIRST_NAME,\n" +
                                "    e1.LAST_NAME,\n" +
                                "    d.DEPARTMENT_NAME,\n" +
                                "    COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT\n" +
                                "FROM\n" +
                                "    EMPLOYEE e1\n" +
                                "JOIN\n" +
                                "    DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID\n" +
                                "LEFT JOIN\n" +
                                "    EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB\n" +
                                "GROUP BY\n" +
                                "    e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME\n" +
                                "ORDER BY\n" +
                                "    e1.EMP_ID DESC;";
            
            System.out.println("Final SQL Query prepared:\n" + finalQuery);

            System.out.println("Step 3: Submitting the solution to the webhook...");
            
            HttpHeaders submitHeaders = new HttpHeaders();
            submitHeaders.setBearerAuth(accessToken);
            submitHeaders.setContentType(MediaType.APPLICATION_JSON);
            
            FinalQueryRequest finalQueryRequest = new FinalQueryRequest(finalQuery);
            HttpEntity<FinalQueryRequest> submitEntity = new HttpEntity<>(finalQueryRequest, submitHeaders);

            ResponseEntity<String> submitResponse = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    submitEntity,
                    String.class
            );

            System.out.println("Submission Status Code: " + submitResponse.getStatusCode());
            System.out.println("Submission Response Body: " + submitResponse.getBody());

        } catch (Exception e) {
            System.err.println("An error occurred during the process: " + e.getMessage());
            e.printStackTrace();
        }
    }
}