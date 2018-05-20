package com.springJadeProject.microservice.service.jade.core.examples.behaviour;

import com.springJadeProject.microservice.service.jade.core.behaviour.BehaviourWithAgentInterface;
import jade.core.Agent;
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
public class ReceiveACLMessageBlockBehaviour extends SimpleBehaviour implements BehaviourWithAgentInterface {

    private boolean fin = false;
    private Agent agent;
    private String content = "It's all good thanks";

    public static ReceiveACLMessageBlockBehaviour getInstance (Agent myAgentIn){
        if(myAgentIn == null){
            return null;
        }else{
            return new ReceiveACLMessageBlockBehaviour(myAgentIn);
        }
    }

    private ReceiveACLMessageBlockBehaviour(Agent myAgentIn){
        agent = myAgentIn;
    }


    /*Required no-argument constructor for being a singleton component*/
    public ReceiveACLMessageBlockBehaviour(){

    }

    @Override
    public void action() {
        fin = false;
        if (agent != null) {
            ACLMessage msg = agent.receive();

            if (msg != null) {
                System.out.println(agent.getLocalName() + ": have just received this msg: ");
                System.out.println(msg.toString());
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(content);
                agent.send(reply);
                System.out.println(agent.getLocalName() + ": sending an answer");
                fin = true;
            } else {
                System.out.println("Waiting for the message");
                System.out.println("Agent who is waiting is: " + agent.getAMS());
                block(); //only block this behaviour until you receive a msg or a explicit wake up
            }

        }

    }


    @Override
    public boolean done() {
        return fin;
    }


    @Override
    public void setNewAgent(Agent agentIn) {
        agent = agentIn;
    }

}