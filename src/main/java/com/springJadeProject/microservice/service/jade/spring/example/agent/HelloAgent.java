package com.springJadeProject.microservice.service.jade.spring.example.agent;

import com.springJadeProject.microservice.service.jade.spring.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.spring.core.agent.SpringAgent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alejandro Reyes
 */

//@Stateless @AgentQualifier(type=AgentType.HELLOAGENT)
@Service
@Qualifier("HelloAgent")
public class HelloAgent extends SpringAgent implements AgentInterface {

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
    }

    @Override
    public AgentInterface getNewInstance() {
        return new HelloAgent();
    }

}
