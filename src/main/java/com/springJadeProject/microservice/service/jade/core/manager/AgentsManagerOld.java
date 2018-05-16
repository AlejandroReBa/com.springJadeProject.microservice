package com.springJadeProject.microservice.service.jade.core.manager;

import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.ejb.Startup;
//import javax.ejb.Singleton;

/**
 *
 * @author Alejandro Reyes
 */


//@Singleton @Startup
@Service
public class AgentsManagerOld {
////    private static AgentsManager agentsManager;
//
//    private static AgentContainer mainContainer;
//    private static Map<String,Agent> agentsOnContainer = new HashMap<>();
//
//    //currently I am not using this singleton initialization. All methods are static
////    public static AgentsManager getAgentsManager(){
////        if (agentsManager == null){
////            agentsManager = new AgentsManager();
////        }
////        return agentsManager;
////    }
//
//    @PostConstruct
//    public void startup(){
//        if (mainContainer == null){
//            initMainContainer();
//        }else{
//            try {
//                mainContainer.start();
//            } catch (ControllerException ex) {
//                Logger.getLogger(AgentsManagerOld.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//    }
//
//    @PreDestroy
//    public void shutdown(){
//        if (mainContainer != null){
//            try {
//                mainContainer.kill();
//            } catch (StaleProxyException ex) {
//                Logger.getLogger(AgentsManagerOld.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//    private static void initMainContainer(){
//        Runtime rt = Runtime.instance();
//        ProfileImpl p = new ProfileImpl(false);
//        mainContainer = rt.createMainContainer(p);
//    }
//
//
//
//    /*
//    public static void addAgentToMainContainer(Agent agent){
//        String nickname = agent.getClass().getSimpleName();
//        addAgentToMainContainer(nickname, agent);
//    }
//    */
//
//    public static void addAgentToMainContainer(String nickname, Agent agent){
//        try {
//            if (!agentsOnContainer.containsKey(nickname) && agent!=null){
//                AgentController agentController = mainContainer.acceptNewAgent(nickname, agent);
//                agentsOnContainer.put(nickname, agent);
//                agentController.start();
//            }else{
//                System.out.println("Agent " + nickname + " is running already");
//            }
//
//        } catch (StaleProxyException ex) {
//            System.out.println("StaleProxyException trying to init again the agent " + nickname + "!");
//            Logger.getLogger(AgentsManagerOld.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//
//    public static void takeDownAgent(String nickname, Agent agent) {
//        try {
//            if (agent == null) {
//                System.out.println("ERROR: Agent is null");
//                //local name is setted by contained when agent is added to it.
//                //then, if it hasn't been added yet or has been deleted already we do not call agent.doDelete
//                //although following API guide it wouldn't have any side effect
//            } else if (agent.getLocalName() != null && mainContainer.getAgentInstance(agent.getLocalName()) != null) {
//                agent.doDelete();
//                deleteAgentFromList(nickname, agent);
//            } else {
//                System.out.println("Agent " + nickname + " is not initiated (or terminated already)");
//            }
//
//        } catch (ControllerException ex) {
//            Logger.getLogger(AgentsManagerOld.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//
//    private static void deleteAgentFromList(String nickname, Agent agent){
//        Agent agentToDelete = agentsOnContainer.get(nickname);
//        if (agentToDelete.equals(agent)){
//            agentsOnContainer.remove(nickname);
//        }
//    }
//
//    //I use status just to make the difference between Agent own Methods and mine.
//    //status and state are not exactly the same but we are using them here at synonyms.
//    public static void checkStatus(Agent agent){
//        System.out.println ("Agent status from agent: " + agent.getState());
//        if (agent.getLocalName() != null){
//            try {
//                AgentController myAgentController = mainContainer.getAgentInstance(agent.getLocalName());
//                if (myAgentController != null){
//                    System.out.println ("Agent " + agent.getLocalName() + " status on container: " + myAgentController.getState());
//                }else{
//                    System.out.println ("Agent " + agent.getLocalName() + " is not in container");
//                }
//            } catch (ControllerException ex) {
//                Logger.getLogger(AgentsManagerOld.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//    }

}
