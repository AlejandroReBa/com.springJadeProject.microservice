package com.springJadeProject.microservice.model.AgentModel;

public class JsonStateAgentModel {
    private String state;

    public JsonStateAgentModel(String state) {
        this.state = state;
    }

    public JsonStateAgentModel() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

