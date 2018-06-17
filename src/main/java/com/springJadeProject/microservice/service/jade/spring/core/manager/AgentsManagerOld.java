package com.springJadeProject.microservice.service.jade.spring.core.manager;

import org.springframework.stereotype.Service;
//import javax.spring.Startup;
//import javax.spring.Singleton;

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

    /*
    public static void changeStateToInititated(Agent agent){
        System.out.println("change state agent -> agentstate1:" + agent.getState());

        //custom inititaed
        LifeCycle customInitiatedLifeCycle = new LifeCycle(Agent.AP_INITIATED){
                        @Override
                        public boolean alive() {
                            return false;
                        }

                        //from this fake initiated state we introduce a intermediate state between
                        //deleted and initiated. Original active is only turned on when we first create Agent
                        //(not needed to be added to container think so) Just in case we can move to ap_active
                        //and original ap_active. Previous to this we were on deleted state and we could not move
                        //from there...so when we try to add again the agent his state was deleted...so same in container
                        @Override
                        public boolean transitionTo(LifeCycle to){
                            return (to.getState() == Agent.AP_ACTIVE || to.getState() == Agent.AP_INITIATED);
                        }
                    };

        agent.changeStateTo(customInitiatedLifeCycle);

        System.out.println("change state agent -> agentstate2:" + agent.getState());
    }
*/

}
