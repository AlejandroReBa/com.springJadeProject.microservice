package com.springJadeProject.microservice.model.agentModel;

import jade.core.behaviours.Behaviour;

import java.util.List;

public class JsonBehavioursModel {
    private String agentName;
    private List<Behaviour> behaviourList;

    public JsonBehavioursModel(String agentName, List<Behaviour> behaviourList){
        this.agentName = agentName;
        this.behaviourList = behaviourList;
    }

    public JsonBehavioursModel(){
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public List<Behaviour> getBehaviourList() {
        return behaviourList;
    }

    public void setBehaviourList(List<Behaviour> behaviourList) {
        this.behaviourList = behaviourList;
    }
}
