package com.springJadeProject.microservice.service.jade.spring.example.agent;


import com.springJadeProject.microservice.service.jade.spring.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.spring.core.agent.SpringAgent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author Alejandro Reyes
 */


@Service
@Qualifier("PlainAgent")
public class PlainAgent extends SpringAgent implements AgentInterface{

    @Override
    protected void setup(){
        super.setup();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:m:ss");
        System.out.println (simpleDateFormat.format(new Date()) + ": Plain Agent " + this.getLocalName() + " is running! :D  -> Welcome!");
    }

    @Override
    protected void takeDown(){
        super.takeDown();
        System.out.println ("Plain Agent " + this.getLocalName() + " has stopped! D: -> Goodbye!");
    }


    @Override
    public AgentInterface getNewInstance() {
        return new PlainAgent();
    }

}
