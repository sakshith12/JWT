package com.bfh.qualifier;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinalQueryRequest {
    @JsonProperty("finalQuery")
    private String finalQuery;

    public FinalQueryRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }

}