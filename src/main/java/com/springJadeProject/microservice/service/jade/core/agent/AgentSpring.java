package com.springJadeProject.microservice.service.jade.core.agent;


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

public abstract class AgentSpring extends Agent implements AgentInterface{
    private String nickname = "";
    private List<Behaviour> behaviourList = new ArrayList<>();

    @Override
    protected void setup(){ /*super() must be called first line in overriden setup() from subclasses */
        for (Behaviour b : behaviourList){
            System.out.println("-->ATACHING BEHAVIOUR: " + b.getBehaviourName());
                /*
                if (b instanceof BehaviourWithAgentInterface){
                    ((BehaviourWithAgentInterface) b).setNewAgent(getAgent());
                    System.out.println("-->YES, IT'S INSTANCEOF BehaviourWithAgentInterface: ");
                }
                */
            addBehaviour(b);
        }

    } //override setup from Agent to force subclasses to implement it

    @Override
    protected abstract void takeDown();


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
                ((BehaviourWithAgentInterface) b).setNewAgent(newInstance.getAgent());
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


    @Override
    public Agent getAgent(){
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

