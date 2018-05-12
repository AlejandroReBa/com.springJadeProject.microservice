package com.springJadeProject.microservice.model.ResponseMessageModel;

import com.springJadeProject.microservice.model.ResponseMessageModel.contract.ResponseMessageInterface;

public class ResponseNotificationMessage implements ResponseMessageInterface {
    private String message;

    public ResponseNotificationMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

