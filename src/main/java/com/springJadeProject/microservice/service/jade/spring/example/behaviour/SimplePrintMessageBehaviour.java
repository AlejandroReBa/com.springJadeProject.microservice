package com.springJadeProject.microservice.service.jade.spring.example.behaviour;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springJadeProject.microservice.service.jade.spring.core.behaviour.BehaviourWithFactoryInterface;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Alejandro Reyes
 */

@Service
@Qualifier("SimplePrintMessageBehaviour")
public class SimplePrintMessageBehaviour extends OneShotBehaviour implements BehaviourWithFactoryInterface {

    @Override
    public Behaviour getInstance() {
        return new SimplePrintMessageBehaviour();
    }

    @Override
    public Behaviour getInstance(Agent agent) {
        return getInstance();
    }

    @Override
    public void action() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:m:ss");
        System.out.println(simpleDateFormat.format(new Date()) + " SimplePrintMessageBehaviour is being executed. This is the only message it will display :D");
    }

    @JsonIgnore
    @Override
    public Agent getAgent() { //required only for adding @JsonIgnore and avoid the behaviour had problems with jackson parse (infinity cycle)
        return super.getAgent();
    }

}
