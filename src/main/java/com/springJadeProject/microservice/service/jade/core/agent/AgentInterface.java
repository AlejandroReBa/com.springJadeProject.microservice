package com.springJadeProject.microservice.service.jade.core.agent;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import java.util.List;

/**
 *
 * @author Alejandro Reyes
 */

//AgentInterface need to be implemented in custom Agents in order to be injected
public interface AgentInterface {

    void init();
    void init(String nickname);
    //void shutDownAgent(); now  we need to return a new instance
    AgentInterface shutDownAgent();
    void checkAgentStatus();
    Agent getAgentInstance();
    //the user can create it owns jade behaviour if he does not want to use our framework
    //we also use this method to attach behaviour when they are created via SpringBehaviour
    boolean addBehaviourToAgent(Behaviour behaviour);
    //addBehaviourToAgentAndInit is used to add a behaviour to an Agent that is already running and we want
    //to execute it in the current execution instead of waiting to the next restart
    boolean addBehaviourToAgentAndInit(Behaviour behaviour);
    boolean resetBehaviourFromAgent(Behaviour behaviour);
    boolean resetBehaviourFromAgentByName(String behaviourName);
    boolean removeBehaviourFromAgent (Behaviour behaviour);
    boolean removeBehaviourFromAgentForever (Behaviour behaviour);
    List<Behaviour> getBehavioursFromAgent();

    void setNickname(String nickname);
    String getNickname();

    //useful to identify which class of agent we have instanced (we can have several agents from same class)
    default String getAgentClassName(){
        return this.getClass().getSimpleName();
    }



    /** ChangeStateTo INITIATED from DELETED it's not possible, so if we want to use this
     * agent again it already have DELETED (4) as state. Won't be initiated when adding it
     * to mainContainer (will be in the container but having deleted state, setup method (from superclass Agent)
     * won't be called and the Agent won't be operated to execute any behaviour...
     * we need the setup for register in yellow pages and initiate all attributes, for example
     *
     * The  best approach at the moment is just to return a new instance of the same type of agent after
     * it is shut down
     *
     **/
}
