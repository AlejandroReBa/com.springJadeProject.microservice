package com.springJadeProject.microservice.model.agentModel;

public class JsonAgentBehaviourModel {
    private String agentClassName;
    private String agentName;
    private String behaviourName;
    //added to start the new behaviour instantly or wait until next restart
    private boolean startNow;
    //added to remove the behaviour from the current runtime (next restart it will be attached again) or forever
    private boolean forever;

    public String getAgentClassName() {
        return agentClassName;
    }

    public void setAgentClassName(String agentClassName) {
        this.agentClassName = agentClassName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getBehaviourName() {
        return behaviourName;
    }

    public void setBehaviourName(String behaviourName) {
        this.behaviourName = behaviourName;
    }

    public boolean getStartNow() {
        return startNow;
    }

    public void setStartNow(boolean startNow) {
        this.startNow = startNow;
    }

    public boolean getForever() {
        return forever;
    }

    public void setForever(boolean forever) {
        this.forever = forever;
    }

}
