package com.springJadeProject.microservice.service.jade.core.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author Alejandro Reyes
 *
 * interface useful to create new instances from the current behaviour in order to add them to distinct agents
 * you only need to inject the behaviour once, but you should never add the inyected behaviour to an agent
 * Be sure of use getInstance instead.
 */

public interface BehaviourWithFactoryInterface {

    @JsonIgnore
    Behaviour getInstance ();

    /**useful to know which agent is bounded to the behaviour
    * If you use a factory it means you are going to manage the behaviour via API
    * so I need to know the agent local name to be displayed via Jackson/JSON **/
    default String getAgentLocalName(){
        String res = null;
        Agent myAgent;
        if (this instanceof Behaviour){
            myAgent = ((Behaviour) this).getAgent();
            if (myAgent != null && myAgent instanceof AgentInterface){
                res = ((AgentInterface)myAgent).getNickname();
            }
        }
        return res;
    }

}
