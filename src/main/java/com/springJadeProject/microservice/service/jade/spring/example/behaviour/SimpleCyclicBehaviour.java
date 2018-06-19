package com.springJadeProject.microservice.service.jade.spring.example.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springJadeProject.microservice.service.jade.spring.core.behaviour.BehaviourWithFactoryInterface;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Qualifier("SimpleCyclicBehaviour")
public class SimpleCyclicBehaviour extends CyclicBehaviour implements BehaviourWithFactoryInterface {

    @Override
    public Behaviour getInstance() {
        return new SimpleCyclicBehaviour();
    }

    @Override
    public Behaviour getInstance(Agent agent) {
        return getInstance();
    }

    @Override
    public void action() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:m:ss");
        System.out.println(simpleDateFormat.format(new Date()) + " Cyclic Behaviour is on! This message will be display forever :D");
    }

    @JsonIgnore
    @Override
    public Agent getAgent() { //required only for adding @JsonIgnore and avoid the behaviour had problems with jackson parse (infinity cycle)
        return super.getAgent();
    }
}
