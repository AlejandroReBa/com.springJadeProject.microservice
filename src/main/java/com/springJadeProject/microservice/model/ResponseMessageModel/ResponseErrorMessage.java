package com.springJadeProject.microservice.model.ResponseMessageModel;

import com.springJadeProject.microservice.model.ResponseMessageModel.contract.ResponseMessageInterface;

public class ResponseErrorMessage implements ResponseMessageInterface{
    private String error;

    public ResponseErrorMessage(String error){
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
