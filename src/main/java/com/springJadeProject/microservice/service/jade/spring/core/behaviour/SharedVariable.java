package com.springJadeProject.microservice.service.jade.spring.core.behaviour;

public class SharedVariable implements SharedVariableInterface {

    private boolean finished;

    public static SharedVariable getSharedVariableInstance(){
        return new SharedVariable();
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
