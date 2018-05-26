package com.springJadeProject.microservice.service.jade.core.behaviour;

import jade.core.Agent;

/**
 *
 * @author Alejandro Reyes
 *
 * interface useful to create behaviours that need a reference to the agent where they are.
 * For example, to send or receive an ACLMessage we need an agent instance
 * The behaviour class must have a Agent variable to set the value passed to setNewAgent method
 */
public interface BehaviourWithAgentInterface {
    /*method available only to re-attach the new instance of Agent by AgentsManager */
    void setNewAgent(Agent agentIn);

}

