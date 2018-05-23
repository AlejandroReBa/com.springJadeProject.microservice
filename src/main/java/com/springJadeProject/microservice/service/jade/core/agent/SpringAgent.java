package com.springJadeProject.microservice.service.jade.core.agent;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springJadeProject.microservice.service.jade.core.behaviour.BehaviourWithAgentInterface;
import com.springJadeProject.microservice.service.jade.core.manager.AgentsManager;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alejandro Reyes
 */

public abstract class SpringAgent extends Agent implements AgentInterface{
    private String nickname = "";
    private List<Behaviour> behaviourList = new ArrayList<>();

//    method to check if this agent is active or not. Useful when from Spring Controller we want
//    to restart (stop and run again) the agent and JADE Container Manager is the performance is not good enough
//    when the agent is shut down we return a new instance, and the initiated variable form the 'old' instance
//    that has been shut down is changed to false. The new one will have initiated as false too.

    private boolean initiated = false;
    public boolean isInitiated (){
        return initiated;
    }

    /**super() must be called first line in overriden setup() from subclasses **/
    @Override
    protected void setup(){
        for (Behaviour b : behaviourList){
            System.out.println("-->ATTACHING BEHAVIOUR: " + b.getBehaviourName());
            addBehaviour(b);
        }

        initiated = true;

    } //override setup from Agent to force subclasses to implement it

    /**not needed to be abstract**/
    @Override
    protected void takeDown(){
        //so we can check certainly if the agent has been takenDown
        initiated = false;
    }//override takeDown from Agent to force subclasses to implement it


    @Override
    public void init() {
        if (nickname.equals("")){
            init(this.getClass().getSimpleName());
        }else{
            init(nickname);
        }

    }

    @Override
    public void init(String nickname) {
        this.nickname = nickname;
        try {
            AgentsManager.addAgentToMainContainer(nickname, this);
        } catch (Exception ex) {
            System.err.println("Seems to have been an exception");
            System.err.println(ex);
        }
    }

    @Override
    public AgentInterface shutDownAgent(){
        AgentsManager.takeDownAgent(nickname, this);
        AgentInterface newInstance = getNewInstance();
        for (Behaviour b : behaviourList){
            b.reset();
            removeBehaviour(b);
            if (b instanceof BehaviourWithAgentInterface){
                ((BehaviourWithAgentInterface) b).setNewAgent(newInstance.getAgentInstance());
                //System.out.println("-->YES, IT'S INSTANCEOF BehaviourWithAgentInterface: ");
            }
            newInstance.addBehaviourToAgent(b);
        }
        return newInstance;
    }

    @Override
    public void checkAgentStatus(){
        AgentsManager.checkStatus(this);
    }


    @JsonIgnore //required to avoid an infinity cycle behaviour when Jackson deserialize an SpringAgent
                //by default using get* named method makes it to consider that we have a property named AgentInterface.
                //As it would be an Agent instance that refers to itself -> we fall into a loop.
    @Override
    public Agent getAgentInstance(){
        return (Agent) this;
    }


    @Override
    public void addBehaviourToAgent(Behaviour behaviour){
        behaviourList.add(behaviour);
    }

    @Override
    public void removeBehaviourFromAgent(Behaviour behaviour){
        if(behaviourList.contains(behaviour)){
            this.removeBehaviour(behaviour);
        }
    }

    @Override
    public void removeBehaviourFromAgentForEver(Behaviour behaviour){
        if (behaviourList.contains(behaviour)){
            behaviourList.remove(behaviour);
            this.removeBehaviour(behaviour);
        }
    }


    @JsonIgnore
    public List<Behaviour> getBehavioursFromAgent(){
        return behaviourList;
    }

    @Override
    public void setNickname(String nicknameIn){
        nickname = nicknameIn;
    }

    @Override
    public String getNickname(){
        String res = this.getClass().getSimpleName();
        if (!nickname.equals("")){
            res = nickname;
        }
        return res;
    }


    /** ChangeStateTo INITIATED from DELETED it's not possible, so if we want to use this
     * agent again it already have DELETED (4) as state. Won't be initiated when adding it
     * to mainContainer (will be in the container but having deleted state, setup method (from superclass Agent)
     * won't be called and the Agent won't be operated to execute any behaviour...
     * we need the setup for register in yellow pages and initiate all attributes, for example
     *
     * The  best approach at the moment is just to return a new instance of the same type of agent after
     * it is shut down
     **/


    /**
     *
     * @return new instance from current class
     */
    protected abstract AgentInterface getNewInstance();

}

