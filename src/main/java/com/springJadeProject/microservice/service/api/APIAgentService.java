package com.springJadeProject.microservice.service.api;

import com.springJadeProject.microservice.service.jade.core.manager.AgentsManager;
import jade.core.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    public Agent findActiveAgentByLocalName(String localName){
        return agentsManager.getAgentsOnContainer().get(localName);
    }


}
