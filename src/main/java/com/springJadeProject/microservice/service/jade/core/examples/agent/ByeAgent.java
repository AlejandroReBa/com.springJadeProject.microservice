package com.springJadeProject.microservice.service.jade.core.examples.agent;

import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.core.agent.AgentSpring;
import org.springframework.stereotype.Component;

/**
 *
 * @author Alejandro Reyes
 */

//@Stateless @AgentQualifier(type=AgentType.BYEAGENT)
@Component
public class ByeAgent extends AgentSpring implements AgentInterface {//Agent implements AgentInterface{

    @Override
    protected void setup() {
        super.setup();
        System.out.println ("------>Hello World from Bye Agent yass :D");
        System.out.println ("My name is " + this.getLocalName());
    }

    @Override
    protected void takeDown() {
        System.out.println("---->ByeAgent: getAID.getName --> " + this.getAID().getName() + " terminating");
    }

    @Override
    protected AgentInterface getNewInstance() {
        return new ByeAgent();
    }


}
