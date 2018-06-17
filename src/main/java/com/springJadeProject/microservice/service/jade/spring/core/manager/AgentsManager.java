package com.springJadeProject.microservice.service.jade.spring.core.manager;

import com.springJadeProject.microservice.service.jade.spring.core.agent.JadeAgentException;
import jade.core.Agent;
import jade.wrapper.*;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Alejandro Reyes
 */


//@Singleton @Startup
@Service
public class AgentsManager {
    private static AgentContainer mainContainer;
    private static Map<String,Agent> agentsOnContainer = new HashMap<>();

    @PostConstruct
    public void startup(){
        if (mainContainer == null){
            createAndInitMainContainer();
        }else{
            try {
                mainContainer.start();
            } catch (ControllerException ex) {
                throw new JadeAgentException("ERROR: ControllerException - " +
                        " main container failed when it was being started -> "
                + ex.getMessage());
            }
        }
    }

    private static void createAndInitMainContainer(){
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl(false);
        mainContainer = runtime.createMainContainer(profile);
    }

    @PreDestroy
    public void shutdown(){
        if (mainContainer != null){
            try {
                mainContainer.kill();
            } catch (StaleProxyException ex) {
                throw new JadeAgentException("ERROR: StaleProxyException - " +
                        "main container failed when it was being finished -> "
                + ex.getMessage());
            }
        }
    }

    public static void addAgentToMainContainer(String nickname, Agent agent){
        try {
            if (!agentsOnContainer.containsKey(nickname) && agent != null
                    && !agentsOnContainer.values().contains(agent)){
                AgentController agentController = mainContainer.acceptNewAgent(nickname, agent);
                agentController.start();
                //add agents to container after it is started, just in case it fails, so we do not fall
                //in an error state where agent is not initiated and it is already added to the list
                agentsOnContainer.put(nickname, agent);
            }else{
                throw new JadeAgentException("ERROR: problem adding an agent to the container:" +
                        " an agent with nickname + " + nickname + " already exist in the system" +
                        " or the agent is null or is already added");
            }
        } catch (StaleProxyException ex) {
            throw new JadeAgentException("ERROR: StaleProxyException - exception trying to init again" +
                    " the agent " + nickname + " ! -> " + ex.getMessage());
        }
    }

    public static void takeDownAgent(String nickname, Agent agent) {
        try {
            if (agent == null) {
                throw new JadeAgentException("ERROR: The agent you are trying to shut down is null");
                //local name is set by contained when agent is added to it.
                //then, if it hasn't been added yet or has been deleted already we do not call
                //agent.doDelete although following API guide it wouldn't have any side effect
            } else if (agent.getLocalName() != null && mainContainer.getAgent(agent.getLocalName()) != null) {
                agent.doDelete();
                deleteAgentFromList(nickname, agent);
            } else {
                throw new JadeAgentException("ERROR: The agent " + nickname + " that you are trying to shut" +
                        " down is not initiated or has been already shutted down");
            }
        } catch (ControllerException ex) {
            throw new JadeAgentException("ERROR: ControllerException - exception shutting down the agent " +
                    nickname + " ! -> " + ex.getMessage());
        }
    }


    private static void deleteAgentFromList(String nickname, Agent agent){
        Agent agentToDelete = agentsOnContainer.get(nickname);
        if (agentToDelete != null && agentToDelete.equals(agent)){
            agentsOnContainer.remove(nickname);
        }
    }

    //Not sure if it should be here (SOLID principles)
    public Map<String,Agent> getAgentsOnContainer (){
        return agentsOnContainer;
    }

}
