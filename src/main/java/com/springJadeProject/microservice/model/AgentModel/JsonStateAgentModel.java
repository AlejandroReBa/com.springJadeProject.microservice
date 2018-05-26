package com.springJadeProject.microservice.model.AgentModel;

public class JsonStateAgentModel {
    private String name;
    private int code;

    public JsonStateAgentModel(String name, int value) {
        this.name = name;
        this.code = value;
    }

    public JsonStateAgentModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

