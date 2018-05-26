package com.springJadeProject.microservice.service.jade.core.examples.behaviour;

import com.springJadeProject.microservice.service.jade.core.agent.SpringJadeAgentException;
import com.springJadeProject.microservice.service.jade.core.behaviour.BehaviourWithAgentInterface;
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
public class ReceiveACLMessageBlockBehaviour extends SimpleBehaviour implements BehaviourWithAgentInterface, BehaviourWithFactoryInterface {

    private boolean finished = false;
    private Agent agent;
    private String content = "It's all good thanks";

//    public static ReceiveACLMessageBlockBehaviour getInstance (Agent myAgentIn){
//        if(myAgentIn == null){
//            return null;
//        }else{
//            return new ReceiveACLMessageBlockBehaviour(myAgentIn);
//        }
//    }

    @Override
    public Behaviour getInstance (Agent myAgentIn){
        if(myAgentIn == null){
            return getInstance();
        }else{
            return new ReceiveACLMessageBlockBehaviour(myAgentIn);
        }
    }

    @Override
    public Behaviour getInstance() {
        return new ReceiveACLMessageBlockBehaviour();
    }

    private ReceiveACLMessageBlockBehaviour(Agent myAgentIn){
        agent = myAgentIn;
    }


    /*Required no-argument constructor for being a singleton component*/
    public ReceiveACLMessageBlockBehaviour(){

    }

    @Override
    public void action() {
        if (agent == null){
            throw new SpringJadeAgentException("Action method from behaviour " + this.getClass().getSimpleName() +
                    " failed because agent reference is null. There are some operations as SendACLMessage that need an agent.");
        }else {
//            finished = false;
            System.out.println ("Action in ReceiveMesageBehaviour is running -> I will try to receive a new message");
            ACLMessage msg = agent.receive();

            if (msg != null) {
                System.out.println(agent.getLocalName() + ": have just received this msg: ");
                System.out.println(msg.toString());
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(content);
                agent.send(reply);
                System.out.println(agent.getLocalName() + ": sending an answer");
                finished = true;
            } else {
                System.out.println("Waiting for the message");
                System.out.println("Agent who is waiting is: " + agent.getAMS());
                block(); //only block this behaviour until you receive a msg or a explicit wake up
            }
        }
    }


    @Override
    public boolean done() {
        return finished;
    }


    @Override
    public void setNewAgent(Agent agentIn) {
        agent = agentIn;
    }

    @Override
    public String getAgentLocalName() {
        String res = null;
        if (agent != null){
            res = agent.getLocalName();
        }
        return res;
    }

    @Override
    public void reset() {
        super.reset();
        this.finished = false;
    }

}