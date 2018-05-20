package com.springJadeProject.microservice.service.jade.core.examples.agent;

import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.core.agent.SpringAgent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alejandro Reyes
 */
//@Stateless @AgentQualifier(type=AgentType.SEND_MESSAGE_AGENT)
@Service
@Qualifier("SendMessageAgent")
public class SendMessageAgent extends SpringAgent implements AgentInterface {

    @Override
    protected void setup() {
        super.setup();
        System.out.println ("------>SendMessageAgent is on setup");
        System.out.println ("My name is " + this.getLocalName());
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        System.out.println("---->SendMessageAgent is on takeDown: terminating");
    }

    @Override
    protected AgentInterface getNewInstance() {
        return new SendMessageAgent();
    }


}
