package com.springJadeProject.microservice.service.jade.core.examples.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springJadeProject.microservice.service.jade.core.agent.JadeAgentException;
import com.springJadeProject.microservice.service.jade.core.behaviour.BehaviourWithFactoryInterface;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alejandro Reyes
 */

@Service
@Qualifier("ReceiveACLMessage")
public class ReceiveACLMessageBlockBehaviour extends SimpleBehaviour implements BehaviourWithFactoryInterface {

    private boolean finished = false;
//    private Agent agent;
    private String content = "It's all good thanks";

    @Override
    public Behaviour getInstance() {
        return new ReceiveACLMessageBlockBehaviour();
    }

    /*Required no-argument constructor for being a singleton component*/
    public ReceiveACLMessageBlockBehaviour(){

    }

    @Override
    public void action() {
        if (myAgent == null){
            throw new JadeAgentException("Action method from behaviour " + this.getClass().getSimpleName() +
                    " failed because agent reference is null. There are some operations as SendACLMessage that need an agent.");
        }else {
//            finished = false;
            System.out.println ("Action in ReceiveMesageBehaviour is running -> I will try to receive a new message");
            ACLMessage msg = myAgent.receive();

            if (msg != null) {
                System.out.println(myAgent.getLocalName() + ": have just received this msg: ");
                System.out.println(msg.toString());
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(content);
                myAgent.send(reply);
                System.out.println(myAgent.getLocalName() + ": sending an answer");
                finished = true;
            } else {
                System.out.println("Waiting for the message");
                System.out.println("Agent who is waiting is: " + myAgent.getLocalName());
                block(); //only block this behaviour until you receive a msg or a explicit wake up
            }
        }
    }


    @Override
    public boolean done() {
        return finished;
    }

    @Override
    public void reset() {
        super.reset();
        this.finished = false;
    }

    @JsonIgnore
    @Override
    public Agent getAgent() { //required only for adding @JsonIgnore and avoid the behaviour had problems with jackson parse (infinity cycle)
        return super.getAgent();
    }
}