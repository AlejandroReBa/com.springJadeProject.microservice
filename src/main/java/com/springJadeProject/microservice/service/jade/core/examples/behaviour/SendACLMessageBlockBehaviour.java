package com.springJadeProject.microservice.service.jade.core.examples.behaviour;

import com.springJadeProject.microservice.service.jade.core.behaviour.BehaviourWithAgentInterface;
import jade.core.AID;
import jade.core.Agent;
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
public class SendACLMessageBlockBehaviour extends SimpleBehaviour implements BehaviourWithAgentInterface {

    private boolean fin = false;
    private Agent agent;
    private String receiverLocalName = "";
    /*Not working because I can only add one sender on matchTemplate to receive the answer*/
    //private List<String> receiverLocalNameList = new ArrayList<>();
    private String language = "English";
    private String ontology = "";
    private String content = "Hey how are you doing?";
    private int performative = ACLMessage.INFORM;

    public static SendACLMessageBlockBehaviour getInstance (Agent myAgentIn){
        if(myAgentIn == null){
            return null;
        }else{
            return new SendACLMessageBlockBehaviour(myAgentIn);
        }
    }

    private SendACLMessageBlockBehaviour(Agent myAgentIn){
        agent = myAgentIn;
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
        fin = false;
        System.out.println(agent.getLocalName() + ": Getting ready to send a message to the receiver");
        AID receiverId = new AID();

        receiverId.setLocalName(receiverLocalName);
        ACLMessage msg = new ACLMessage(performative);
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
        agent.send(msg);

        System.out.println("Sending greetings to receiver");
        System.out.println("Waiting for a response from receiver: fin value: " + fin);
        System.out.println("SEND AGENT: receiverLocalName -> " + receiverLocalName);

        //actually should check for msgs coming only from receiver
        MessageTemplate senderFilter = MessageTemplate.MatchSender(receiverId);

        MessageTemplate performativeFilter = MessageTemplate.MatchPerformative(performative);
        MessageTemplate languageFilter = MessageTemplate.MatchLanguage(language);

        MessageTemplate template = MessageTemplate.and(senderFilter, performativeFilter);
        template = MessageTemplate.and(template, languageFilter);

        ACLMessage msgResponse = agent.blockingReceive(template);
        System.out.println(agent.getLocalName() + ": have just received the response: ");
        System.out.println(msgResponse.toString());

        fin = true;
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
