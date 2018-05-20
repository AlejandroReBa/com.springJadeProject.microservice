package com.springJadeProject.microservice.service.jade.core.behaviour;

import jade.core.behaviours.Behaviour;

/**
 *
 * @author Alejandro Reyes
 */

public abstract class SpringBehaviour {
    /**Consumer if it returns nothing -> https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html
     * Supplier if it takes nothing -> https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html
     * Runnable if it does neither -> https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html
     * Function if it takes and returns
     */

    protected Behaviour currentBehaviour; //SimpleBehaviour = oneShotBehaviour + cyclic behaviour

    public interface ActionInterface {
        void action();
    }

    //https://www.javabrahman.com/java-8/java-8-java-util-function-function-tutorial-with-examples/
}
