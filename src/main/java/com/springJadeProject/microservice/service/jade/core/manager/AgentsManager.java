package com.springJadeProject.microservice.service.jade.core.manager;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.wrapper.*;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author Alejandro Reyes
 */


//@Singleton @Startup
@Service
public class AgentsManager {
//    private static AgentsManager agentsManager;

    private static AgentContainer mainContainer;
    private static Map<String,Agent> agentsOnContainer = new HashMap<>();

    //currently I am not using this singleton initialization. All methods are static
//    public static AgentsManager getAgentsManager(){
//        if (agentsManager == null){
//            agentsManager = new AgentsManager();
//        }
//        return agentsManager;
//    }

    @PostConstruct
    public void startup(){
        if (mainContainer == null){
            initMainContainer();
        }else{
            try {
                mainContainer.start();
            } catch (ControllerException ex) {
                Logger.getLogger(AgentsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @PreDestroy
    public void shutdown(){
        if (mainContainer != null){
            try {
                mainContainer.kill();
            } catch (StaleProxyException ex) {
                Logger.getLogger(AgentsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void initMainContainer(){
        Runtime rt = Runtime.instance();
        ProfileImpl p = new ProfileImpl(false);
        mainContainer = rt.createMainContainer(p);
    }



    /*
    public static void addAgentToMainContainer(Agent agent){
        String nickname = agent.getClass().getSimpleName();
        addAgentToMainContainer(nickname, agent);
    }
    */

    public static void addAgentToMainContainer(String nickname, Agent agent){
        try {
            if (!agentsOnContainer.containsKey(nickname) && agent!=null){
                //AMSAgent just initiated once, so we do not have this problem.
                //In the same way we use it to check if a deleted agent via agent.doDelete()
                //is still in the container (It may happens when we stop and init the same agent in a row)
//                checkAgentIsNotInContainer(nickname);

//                while (mainContainer.getAgent(nickname) != null){ --> it raises an exception
//                    //In some specific situations when we stop and init again the same agent at the moment
//                    //we need to give some time to container to delete de agent. Otherwise a StaleProxyException
//                    //will be raised
//                    System.out.println("Waiting to MainContainer. Still deleting the agent we want to init here!");
//                }
//                AgentController agentController = null;
//                int index = 0;
//                while (agentController == null){
//                    index++;
//                    System.out.println("Accepting new agent. Try number: " + index);
//                    agentController = customAcceptNewAgent(nickname, agent);
////                    mainContainer.acceptNewAgent(nickname, agent);
//                }
                AgentController agentController = mainContainer.acceptNewAgent(nickname, agent);

//                System.out.println ( "222---> " + mainContainer.getAgent(nickname).getState() + " --> " + mainContainer.getAgent(nickname).getName());

                agentController.start();
                //add agents to container after it is started, just in case it fails, so we do not fall in an error state
                //where agent is not initiated and it is already added to the list
                agentsOnContainer.put(nickname, agent);
            }else{
                System.out.println("Agent " + nickname + " is running already");
            }

        } catch (StaleProxyException ex) {
            System.out.println("StaleProxyException trying to init again the agent " + nickname + "!");
            Logger.getLogger(AgentsManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    private static AgentController customAcceptNewAgent(String nickname, Agent agent) {
//        AgentController agentController = null;
//        try {
//            agentController = mainContainer.acceptNewAgent(nickname, agent);
//        } catch (StaleProxyException e) {
//            e.printStackTrace();
//        }
//        return agentController;
//    }

    public static void takeDownAgent(String nickname, Agent agent) {
        try {
            if (agent == null) {
                System.out.println("ERROR: Agent is null");
                //local name is setted by contained when agent is added to it.
                //then, if it hasn't been added yet or has been deleted already we do not call agent.doDelete
                //although following API guide it wouldn't have any side effect
            } else if (agent.getLocalName() != null && mainContainer.getAgent(agent.getLocalName()) != null) {
                agent.doDelete();
                deleteAgentFromList(nickname, agent);
            } else {
                System.out.println("Agent " + nickname + " is not initiated (or terminated already)");
            }

        } catch (ControllerException ex) {
            System.out.println ("Exception taking down the agent " + nickname);
            Logger.getLogger(AgentsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static void deleteAgentFromList(String nickname, Agent agent){
        Agent agentToDelete = agentsOnContainer.get(nickname);
        if (agentToDelete != null && agentToDelete.equals(agent)){
            agentsOnContainer.remove(nickname);
        }
    }

    //I use status just to make the difference between Agent own Methods and mine.
    //status and state are not exactly the same but we are using them here at synonyms.
    public static void checkStatus(Agent agent){
        System.out.println ("Agent status from agent: " + agent.getState());
        if (agent.getLocalName() != null){
            try {
                AgentController myAgentController = mainContainer.getAgent(agent.getLocalName());
                if (myAgentController != null){
                    System.out.println ("Agent " + agent.getLocalName() + " status on container: " + myAgentController.getState());
                }else{
                    System.out.println ("Agent " + agent.getLocalName() + " is not in container");
                }
            } catch (ControllerException ex) {
                Logger.getLogger(AgentsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

//    private static void checkAgentIsNotInContainer (String nickname){
//        AMSAgentDescription[] agentDescriptionList = {};
//        try
//        {
//            SearchConstraints restrictions = new SearchConstraints();
//            restrictions.setMaxResults ( -1L);
//            //I think new Agent() should be already initiated in the container...
//            agentDescriptionList = AMSService.search(new Agent(), new AMSAgentDescription(), restrictions);
//        }
//        catch (Exception e) { System.out.println(e); }
//
//        for (AMSAgentDescription agentDescription : agentDescriptionList) {
//            if (agentDescription.getName().getLocalName().equals(nickname) {
//              ///////
//            }
//
//        }
//
//    }


//    //Not sure if it should be here (SOLID principles)
//    public static List<Agent> getAgentsOnContainer (){
//        Collection<Agent> agentCollection = agentsOnContainer.values();
//        Agent AMSAgent = agentsOnContainer.get("AMSAgent");
//        agentCollection.remove(AMSAgent);
//        return new ArrayList<>(agentCollection);
//    }

    //Not sure if it should be here (SOLID principles)
    public Map<String,Agent> getAgentsOnContainer (){
        return agentsOnContainer;
    }

}
