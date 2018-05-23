package com.springJadeProject.microservice.service.jade.core.behaviour;

import jade.core.Agent;

/**
 *
 * @author Alejandro Reyes
 */
public interface BehaviourWithAgentInterface {
    /*method available only to re-attach the new instance of Agent by AgentsManager */
    void setNewAgent(Agent agentIn);

    /*useful to know which agent is bounded to the behaviour*/
    String getAgentLocalName(); //useful to know which agent is bounded to the behaviour
}