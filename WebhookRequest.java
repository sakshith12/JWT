package com.bfh.qualifier;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebhookRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("regNo")
    private String regNo;
    @JsonProperty("email")
    private String email;

    public WebhookRequest(String name, String regNo, String email) {
        this.name = name;
        this.regNo = regNo;
        this.email = email;
    }
}