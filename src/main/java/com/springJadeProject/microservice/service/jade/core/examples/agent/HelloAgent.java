package com.springJadeProject.microservice.service.jade.core.examples.agent;

import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.core.agent.AgentSpring;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alejandro Reyes
 */

//@Stateless @AgentQualifier(type=AgentType.HELLOAGENT)
@Service
@Qualifier("HelloAgent")
public class HelloAgent extends AgentSpring implements AgentInterface {

    @Override
    protected void setup(){
        super.setup();
        System.out.println ("------>Hello World from Hello Agent yass :D");
        System.out.println ("My name is " + this.getLocalName());
    }

    @Override
    protected void takeDown(){
        super.takeDown();
        System.out.println("---->HelloAgent: getAID.getName --> " + this.getAID().getName() + " terminating");
        //AgentsManager.deleteAgentFromList(nickname, this);
    }

    @Override
    protected AgentInterface getNewInstance() {
        return new HelloAgent();
    }
    }
