package com.springJadeProject.microservice.service.jade.spring.core.behaviour;

/**
 *
 * @author Alejandro Reyes
 */
public class SharedVariableInteger implements SharedVariableInterface {
    private static final int MIN_VALUE = 0;

    private int currentValue; //current value used to choose the current state of the behaviour
    private int valueToFinish; //value to check if last state of behaviour has been reached

    public static SharedVariableInteger getSharedVariableInstance(int valueToFinish){
        if(valueToFinish < MIN_VALUE){
            valueToFinish = MIN_VALUE;
        }
        return new SharedVariableInteger(valueToFinish);
    }

    private SharedVariableInteger(int valueToFinishIn){
        currentValue = MIN_VALUE;
        valueToFinish = valueToFinishIn;
    }

    @Override
    public boolean isFinished() {
        return currentValue == valueToFinish;
    }

    @Override
    public void setFinished(boolean finished) {
        if (finished){
            currentValue = valueToFinish;
        }else{
            currentValue = MIN_VALUE;
        }
    }

    public void setCurrentValue(int currentValueIn) {
        currentValue = currentValueIn;
    }

    public int getCurrentValue() {
        return currentValue;
    }

}

