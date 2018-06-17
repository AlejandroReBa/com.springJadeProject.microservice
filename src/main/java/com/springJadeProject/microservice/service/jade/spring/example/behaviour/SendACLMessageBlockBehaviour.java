package com.springJadeProject.microservice.service.jade.spring.example.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springJadeProject.microservice.service.jade.spring.core.agent.JadeAgentException;
import com.springJadeProject.microservice.service.jade.spring.core.behaviour.BehaviourWithFactoryInterface;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alejandro Reyes
 *
 * about LocalBean: https://stackoverflow.com/questions/34411371/is-it-possible-to-inject-ejb-implementation-and-not-its-interface-using-cdi?noredirect=1&lq=1
 */

@Service
@Qualifier("SendACLMessage")
public class SendACLMessageBlockBehaviour extends SimpleBehaviour implements BehaviourWithFactoryInterface {

    private boolean finished = false;
    private boolean waitingResponse = false;
//    private Agent agent;
    private String receiverLocalName = "ReceiveMessageAgent";
    /*Not working because I can only add one sender on matchTemplate to receive the answer*/
    //private List<String> receiverLocalNameList = new ArrayList<>();
    private String language = "English";
    private String ontology = "";
    private String content = "Hey how are you doing?";
    private int performative = ACLMessage.INFORM;

    @Override
    public Behaviour getInstance() {
        return new SendACLMessageBlockBehaviour();
    }

    /*Required no-argument constructor for being a singleton component*/
    public SendACLMessageBlockBehaviour(){
    }

    public void setReceiverLocalName (String receiverLocalNameIn){
        //receiverLocalNameList.add(receiverLocalNameIn);
        receiverLocalName = receiverLocalNameIn;
    }

    public void setLanguage (String languageIn){
        language = languageIn;
    }

    @Override
    public void action() {
        AID receiverId = new AID();
        receiverId.setLocalName(receiverLocalName);
        ACLMessage msg;
        MessageTemplate senderFilter;
        MessageTemplate performativeFilter;
        MessageTemplate languageFilter;
        MessageTemplate template;
        ACLMessage msgResponse;

        if (myAgent == null){
            throw new JadeAgentException("Action method from behaviour " + this.getClass().getSimpleName() +
            " failed because agent reference is null. There are some operations as SendACLMessage that need an agent.");
        }else if (receiverLocalName == null){
            throw new JadeAgentException("Action method from behaviour " + this.getClass().getSimpleName() +
                    " failed because the local name of the message receiver specified is null.");
        }else if (!waitingResponse) {
//            finished = false;
            System.out.println(myAgent.getLocalName() + ": Getting ready to send a message to the receiver");
            msg = new ACLMessage(performative);
            //msg.setLanguage("English");
            msg.addReceiver(receiverId);
            msg.setLanguage(language);
        /*
        for (String str : receiverLocalNameList){
            receiverId.setLocalName(str);
            msg.addReceiver(receiverId);
        }
*/

            //msg.setContent("Hey there! how are you doing?");
            msg.setContent(content);
            myAgent.send(msg);
            waitingResponse = true;

            System.out.println("Sending greetings to receiver");
            System.out.println("Waiting for a response from receiver. finished value: " + finished);
            System.out.println("SEND AGENT: receiverLocalName -> " + receiverLocalName);

        }
            //actually should check for msgs coming only from receiver
            senderFilter = MessageTemplate.MatchSender(receiverId);

            performativeFilter = MessageTemplate.MatchPerformative(performative);
            languageFilter = MessageTemplate.MatchLanguage(language);

            template = MessageTemplate.and(senderFilter, performativeFilter);
            template = MessageTemplate.and(template, languageFilter);

//            ACLMessage msgResponse = agent.blockingReceive(template);
            msgResponse = myAgent.receive(template);

            if (msgResponse != null){
                System.out.println(myAgent.getLocalName() + ": have just received the response: ");
                System.out.println(msgResponse.toString());
                finished = true;
                waitingResponse = false;
                //send message is null ? how to checked it to avoid send to itself?...
            }else{
                System.out.println("Waiting for the response message");
                System.out.println("Agent who is waiting is: " + myAgent.getLocalName());
                block(); //only block this behaviour until you receive a msg or a explicit wake up
            }

    }

    @Override
    public boolean done() {
        return finished;
    }

//    @Override
//    public void setNewAgent(Agent agentIn) {
//        agent = agentIn;
//    }

    @Override
    public void reset() {
        super.reset();
        this.finished = false;
        this.waitingResponse = false;
    }

    @JsonIgnore
    @Override
    public Agent getAgent() { //required only for adding @JsonIgnore and avoid the behaviour had problems with jackson parse (infinity cycle)
        return super.getAgent();
    }


}
