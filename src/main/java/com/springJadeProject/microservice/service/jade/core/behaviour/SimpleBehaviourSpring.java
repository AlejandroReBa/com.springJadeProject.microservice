package com.springJadeProject.microservice.service.jade.core.behaviour;

import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import jade.core.behaviours.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Alejandro Reyes
 *
 * This is a factory class to create custom behaviours in an easy way if you are new into JADE
 * Only very simple behaviours can be implemented this way.
 */

@Service
@Qualifier("SimpleBehaviourSpring")
public class SimpleBehaviourSpring extends SpringBehaviour {
    public void addOneShotBehaviour(ActionInterface actionInterface, AgentInterface agentInterface) {
        currentBehaviour = new OneShotBehaviour() {
            @Override
            public void action() {
                actionInterface.action();
            }
        };
        agentInterface.addBehaviourToAgent(currentBehaviour);
    }


    public Behaviour addCyclicBehaviour(ActionInterface actionInterface, AgentInterface agentInterface) {
        currentBehaviour = new CyclicBehaviour() {
            @Override
            public void action() {
                actionInterface.action();
            }
        };

        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addTickerBehaviour(ActionInterface actionInterface, AgentInterface agentInterface, long period) {

        currentBehaviour = new TickerBehaviour(agentInterface.getAgentInstance(), period) {
            @Override
            protected void onTick() {
                actionInterface.action();
            }
        };
        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addWakerBehaviour(ActionInterface actionInterface, AgentInterface agentInterface, long timeout) {

        currentBehaviour = new WakerBehaviour(agentInterface.getAgentInstance(), timeout) {
            /*old method
            @Override
           protected void handleElapsedTimeout(){
                actionInterface.action();
           }
            */

            @Override
            protected void onWake() {
                actionInterface.action();
            }
        };
        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addWakerBehaviour(ActionInterface actionInterface, AgentInterface agentInterface, java.util.Date wakeupDate) {

        currentBehaviour = new WakerBehaviour(agentInterface.getAgentInstance(), wakeupDate) {
             /*old method
            @Override
           protected void handleElapsedTimeout(){
                actionInterface.action();
           }
           */

            @Override
            protected void onWake() {
                actionInterface.action();
            }
        };
        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addSimpleBehaviour(Consumer<SharedVariable> actionIn,
                                        Function<SharedVariable, Boolean> doneIn, SharedVariable sharedVariable,
                                        AgentInterface agentInterface) {

        currentBehaviour = new SimpleBehaviour() {
            @Override
            public void action() {
                actionIn.accept(sharedVariable);
            }

            @Override
            public boolean done() {
                return doneIn.apply(sharedVariable);
            }
        };

        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addSimpleBehaviour(Consumer<SharedVariableInteger> actionIn,
                                        Function<SharedVariableInteger, Boolean> doneIn, SharedVariableInteger sharedVariable,
                                        AgentInterface agentInterface) {

        currentBehaviour = new SimpleBehaviour() {
            @Override
            public void action() {
                actionIn.accept(sharedVariable);
            }

            @Override
            public boolean done() {
                boolean res = doneIn.apply(sharedVariable);
                if (res){
                    /**reinitiate the variable which determines the state the behaviour is in
                     * so when we shutdown and restart the agent this method does not return true
                     * since the beginning
                     */
                    sharedVariable.setCurrentValue(0);
                }
                return res;
            }
        };

        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


}
