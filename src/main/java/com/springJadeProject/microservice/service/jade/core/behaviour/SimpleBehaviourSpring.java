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
    public Behaviour addOneShotBehaviour(ActionInterface actionInterface, AgentInterface agentInterface) {
        currentBehaviour = new OneShotBehaviour() {
            private String agentLocalName;

            @Override
            public void action() {
                if (myAgent != null){
                    this.agentLocalName = myAgent.getLocalName();
                }
                actionInterface.action();
            }

            public String getAgentLocalName(){
                return agentLocalName;
            }
        };
        agentInterface.addBehaviourToAgent(currentBehaviour);

        return currentBehaviour;
    }


    public Behaviour addCyclicBehaviour(ActionInterface actionInterface, AgentInterface agentInterface) {
        currentBehaviour = new CyclicBehaviour() {
            private String agentLocalName;

            @Override
            public void action() {
                if (myAgent != null){
                    this.agentLocalName = myAgent.getLocalName();
                }
                actionInterface.action();
            }

            public String getAgentLocalName(){
                return agentLocalName;
            }
        };

        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addTickerBehaviour(ActionInterface actionInterface, AgentInterface agentInterface, long period) {

        currentBehaviour = new TickerBehaviour(agentInterface.getAgentInstance(), period) {
            private String agentLocalName;

            @Override
            protected void onTick() {
                if (myAgent != null){
                    this.agentLocalName = myAgent.getLocalName();
                }
                actionInterface.action();
            }

            public String getAgentLocalName(){
                return agentLocalName;
            }

        };
        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addWakerBehaviour(ActionInterface actionInterface, AgentInterface agentInterface, long timeout) {

        currentBehaviour = new WakerBehaviour(agentInterface.getAgentInstance(), timeout) {
            private String agentLocalName;
            /*old method
            @Override
           protected void handleElapsedTimeout(){
                actionInterface.action();
           }
            */

            @Override
            protected void onWake() {
                if (myAgent != null){
                    this.agentLocalName = myAgent.getLocalName();
                }
                actionInterface.action();
            }

            public String getAgentLocalName(){
                return agentLocalName;
            }
        };
        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addWakerBehaviour(ActionInterface actionInterface, AgentInterface agentInterface, java.util.Date wakeupDate) {

        currentBehaviour = new WakerBehaviour(agentInterface.getAgentInstance(), wakeupDate) {
            private String agentLocalName;
             /*old method
            @Override
           protected void handleElapsedTimeout(){
                actionInterface.action();
           }
           */

            @Override
            protected void onWake() {
                if (myAgent != null){
                    this.agentLocalName = myAgent.getLocalName();
                }
                actionInterface.action();
            }

            public String getAgentLocalName(){
                return agentLocalName;
            }
        };
        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addSimpleBehaviour(Consumer<SharedVariable> actionIn,
                                        Function<SharedVariable, Boolean> doneIn, SharedVariable sharedVariable,
                                        AgentInterface agentInterface) {

        currentBehaviour = new SimpleBehaviour() {
            private String agentLocalName;
            @Override
            public void action() {
                if (myAgent != null){
                    this.agentLocalName = myAgent.getLocalName();
                }
                actionIn.accept(sharedVariable);
            }

            @Override
            public boolean done() {
                return doneIn.apply(sharedVariable);
            }

            public String getAgentLocalName(){
                return agentLocalName;
            }
        };

        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


    public Behaviour addSimpleBehaviour(Consumer<SharedVariableInteger> actionIn,
                                        Function<SharedVariableInteger, Boolean> doneIn, SharedVariableInteger sharedVariable,
                                        AgentInterface agentInterface) {

        currentBehaviour = new SimpleBehaviour() {
            private String agentLocalName;

            @Override
            public void action() {
                if (myAgent != null){
                    this.agentLocalName = myAgent.getLocalName();
                }
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

            public String getAgentLocalName(){
                return agentLocalName;
            }
        };

        agentInterface.addBehaviourToAgent(currentBehaviour);
        return currentBehaviour;
    }


}
