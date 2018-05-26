package com.springJadeProject.microservice.model.AgentModel;

public class JsonAgentBehaviourModel {
    private String agentName;
    private String behaviourName;
    //added to start the new behaviour instantly or wait until next restart
    private Boolean startNow;
    //added to remove the behaviour from the current runtime (next restart it will be attached again) or forever
    private Boolean forever;

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

    public Boolean getStartNow() {
        return startNow;
    }

    public void setStartNow(Boolean startNow) {
        this.startNow = startNow;
    }

    public Boolean getForever() {
        return forever;
    }

    public void setForever(Boolean forever) {
        this.forever = forever;
    }

}
