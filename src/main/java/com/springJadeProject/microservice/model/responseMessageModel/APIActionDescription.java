package com.springJadeProject.microservice.model.responseMessageModel;

public class APIActionDescription {
    private String path;
    private String method;
    private String description;
    private String parameters;
    private String result;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public APIActionDescription(String path, String method, String description) {
        this.path = path;
        this.method = method;
        this.description = description;
    }

    public APIActionDescription(String path, String method, String description, String parameters, String result) {
        this.path = path;
        this.method = method;
        this.description = description;
        this.parameters = parameters;
        this.result = result;

    }
}
