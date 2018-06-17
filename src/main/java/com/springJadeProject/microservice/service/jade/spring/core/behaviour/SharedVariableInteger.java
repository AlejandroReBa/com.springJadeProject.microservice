package com.springJadeProject.microservice.service.jade.spring.core.behaviour;

/**
 *
 * @author Alejandro Reyes
 */
public class SharedVariableInteger implements SharedVariableInterface {

    private int currentValue; //current value used to choose the current state of the behaviour
    private int valueToFinish; //value to check if last state of behaviour has been reached in order to end it

    public static SharedVariableInteger getSharedVariableInstance(int valueToFinish){
        if(valueToFinish < 0 ){
            return new SharedVariableInteger(0);
            /*
            throw new RuntimeException("ERROR: 'vallueToFinish from getSharedVariableInstance method"
                    + " should be a positive number");
            */
        }
        return new SharedVariableInteger(valueToFinish);
    }

    private SharedVariableInteger(int valueToFinishIn){
        currentValue = 0;
        valueToFinish = valueToFinishIn;
    }

    @Override
    public boolean isFinished() {
        return currentValue == valueToFinish;
    }

    @Override
    public void setFinished(boolean finished) {
        if (finished){
            this.currentValue = valueToFinish;
        }
    }

    public void setCurrentValue(int currentValueIn) {
        currentValue = currentValueIn;
    }

    public int getCurrentValue() {
        return currentValue;
    }

}

