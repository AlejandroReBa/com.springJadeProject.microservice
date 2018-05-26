package com.springJadeProject.microservice.service.api;

import com.springJadeProject.microservice.service.jade.core.agent.SpringAgent;
import com.springJadeProject.microservice.service.jade.core.manager.AgentsManager;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class APIAgentService {

    @Autowired
    private AgentsManager agentsManager; //use to access the agents running on the container

    public APIAgentService() {
    }

    public List<Agent> findActiveAgents(){
        Collection<Agent> agentCollection = agentsManager.getAgentsOnContainer().values();
        Agent AMSAgent = agentsManager.getAgentsOnContainer().get("AMSAgent");
        agentCollection.remove(AMSAgent);
        return new ArrayList<>(agentCollection);
    }

    public List<String> findActiveAgentsName(){
        return new ArrayList<>(agentsManager.getAgentsOnContainer().keySet());
    }

    public Agent findActiveAgentByLocalName(String localName){
        return agentsManager.getAgentsOnContainer().get(localName);
    }

    /**Field Agent from behaviour will be always null from API to avoid Jackson serialization infinity loop
     * due to myAgent variable. @JsonIgnore would fix this problem but we don't want to modify JADE library*/
    public List<Behaviour> findBehavioursFromAgentByLocalName(String localName){
        return this.getBehavioursFromAgentWithAgentNullAndStateByLocalName(localName, null);
    }

    public List<String> findBehavioursNameFromAgentByLocalName(String localName){
        return this.getBehavioursNameFromAgentWithStateByLocalName(localName, null);
    }

    public List<Behaviour> findReadyBehavioursFromAgentByLocalName(String localName){
        return this.getBehavioursFromAgentWithAgentNullAndStateByLocalName(localName, Behaviour.STATE_READY);
    }

    public List<String> findReadyBehavioursNameFromAgentByLocalName(String localName){
        return this.getBehavioursNameFromAgentWithStateByLocalName(localName, Behaviour.STATE_READY);
    }

    public List<Behaviour> findBlockedBehavioursFromAgentByLocalName(String localName){
        return this.getBehavioursFromAgentWithAgentNullAndStateByLocalName(localName, Behaviour.STATE_BLOCKED);
    }

    public List<String> findBlockedBehavioursNameFromAgentByLocalName(String localName){
        return this.getBehavioursNameFromAgentWithStateByLocalName(localName, Behaviour.STATE_BLOCKED);
    }

    public List<Behaviour> findRunningBehavioursFromAgentByLocalName(String localName){
        return this.getBehavioursFromAgentWithAgentNullAndStateByLocalName(localName, Behaviour.STATE_RUNNING);
    }

    public List<String> findRunningBehavioursNameFromAgentByLocalName(String localName){
        return this.getBehavioursNameFromAgentWithStateByLocalName(localName, Behaviour.STATE_RUNNING);
    }

    private List<Behaviour> getBehavioursFromAgentByLocalName(String localName){
        List<Behaviour> result = new ArrayList<>();
        Agent agent = findActiveAgentByLocalName(localName);

        if (agent != null){
            result = ((SpringAgent) agent).getBehavioursFromAgent();
        }
        return result;
    }

    private List<Behaviour> getBehavioursFromAgentWithStateByLocalName(String localName, String state){
        List<Behaviour> result;
        List<Behaviour> behavioursList = this.getBehavioursFromAgentByLocalName(localName);

        if (state == null){
//            result = new ArrayList<>(behavioursList);
            result = behavioursList;
        }else{
            result = new ArrayList<>();
            for (Behaviour behaviour : behavioursList) {
                if (behaviour.getExecutionState().equals(state)) {
                    result.add(behaviour);
                }
            }
        }
        return result;
    }

    private List<Behaviour> getBehavioursFromAgentWithAgentNullAndStateByLocalName(String localName, String state){
        //we create a copy via new ArrayList<>(Collection<>), in order to not modify the original
        List<Behaviour> result = new ArrayList<>(this.getBehavioursFromAgentWithStateByLocalName(localName, state));

        for (Behaviour behaviour : result){
            behaviour.setAgent(null);
        }
        return result;
    }

    private List<String> getBehavioursNameFromAgentWithStateByLocalName(String localName, String state){
        List<String> result = new ArrayList<>();
        List<Behaviour> behavioursList = this.getBehavioursFromAgentWithStateByLocalName(localName, state);

        for (Behaviour behaviour : behavioursList){
            result.add(behaviour.getBehaviourName());
        }
        return result;
    }


}
