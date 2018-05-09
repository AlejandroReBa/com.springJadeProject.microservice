package com.springJadeProject.microservice.service.jade.core.examples.agent;

import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.core.agent.AgentSpring;
import org.springframework.stereotype.Component;

/**
 *
 * @author Alejandro Reyes
 */
//@Stateless @AgentQualifier(type=AgentType.SEND_MESSAGE_AGENT)
@Component
public class SendMessageAgent extends AgentSpring implements AgentInterface {

    @Override
    protected void setup() {
        super.setup();
        System.out.println ("------>SendMessageAgent is on setup");
        System.out.println ("My name is " + this.getLocalName());
    }

    @Override
    protected void takeDown() {
        System.out.println("---->SendMessageAgent is on takeDown: terminating");
    }

    @Override
    protected AgentInterface getNewInstance() {
        return new SendMessageAgent();
    }


}
