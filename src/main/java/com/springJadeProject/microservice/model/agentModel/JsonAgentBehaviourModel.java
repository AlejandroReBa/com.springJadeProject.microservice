package com.springJadeProject.microservice.model.agentModel;

public class JsonAgentBehaviourModel {
    public static final String INIT = "init";
    public static final String STOP = "stop";
    public static final String RESTART = "restart";
    public static final String ADD = "add";
    public static final String RESET = "reset";
    public static final String REMOVE = "remove";

    private String className;
    private String agentName;
    private String behaviourName;
    private String action;
    //added to start the new behaviour instantly or wait until next restart
    private boolean startNow;
    //added to remove the behaviour from the current runtime
    //(next restart it will be attached again) or forever
    private boolean forever;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
