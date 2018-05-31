package com.springJadeProject.microservice.controller;

import com.springJadeProject.microservice.model.agentModel.JsonAgentBehaviourModel;
import com.springJadeProject.microservice.model.agentModel.JsonBehavioursModel;
import com.springJadeProject.microservice.model.agentModel.JsonStateAgentModel;
import com.springJadeProject.microservice.model.responseMessageModel.APIActionDescription;
import com.springJadeProject.microservice.model.responseMessageModel.ResponseErrorMessage;
import com.springJadeProject.microservice.model.responseMessageModel.ResponseNotificationMessage;
import com.springJadeProject.microservice.model.responseMessageModel.contract.ResponseMessageInterface;
import com.springJadeProject.microservice.service.api.APIAgentService;
import com.springJadeProject.microservice.service.api.APIDescriptionService;
import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.core.agent.SpringAgent;
import com.springJadeProject.microservice.service.jade.core.behaviour.BehaviourWithFactoryInterface;
import com.springJadeProject.microservice.service.jade.core.behaviour.SimpleBehaviourSpring;
import com.springJadeProject.microservice.service.jade.core.behaviour.SpringBehaviour;
import com.springJadeProject.microservice.service.jade.core.examples.behaviour.ReceiveACLMessageBlockBehaviour;
import com.springJadeProject.microservice.service.jade.core.examples.behaviour.SendACLMessageBlockBehaviour;
import com.springJadeProject.microservice.service.jade.core.manager.AMSAgent;
import jade.core.Agent;
import jade.core.AgentState;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.wrapper.ControllerException;
import jade.wrapper.State;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api")
public class JadeRestController {

    /**Inject your agents via @Autowired below**/
    @Autowired
    @Qualifier("HelloAgent")
    private AgentInterface helloAgent;

    @Autowired
    @Qualifier("SpringAgent")
    private AgentInterface byeAgent;

    @Autowired
    @Qualifier("ReceiveMessageAgent")
    private AgentInterface receiveMessageAgent;

    @Autowired
    @Qualifier("SendMessageAgent")
    private AgentInterface sendMessageAgent;
    /**Inject your agents via @Autowired above*/

    /**Inject your behaviors via @Autowired below**/
    @Autowired
    @Qualifier("SimpleBehaviourSpring")
    private SimpleBehaviourSpring simpleBehaviourSpring;

    @Autowired
    @Qualifier("ReceiveACLMessage")
    ReceiveACLMessageBlockBehaviour receiveACLMessageBlockBehaviour;

    @Autowired
    @Qualifier("SendACLMessage")
    SendACLMessageBlockBehaviour sendACLMessageBlockBehaviour;


    /**Inject your behaviors via @Autowired above**/


    /**Injections required by the framework below**/
    @Autowired
    private APIAgentService apiAgentService;

    @Autowired
    private AMSAgent amsAgent;

    @Autowired
    private APIDescriptionService apiDescriptionService;
    /**Injections required by the framework above**/


    /**local variables below**/
    private Map<String, AgentInterface> availableAgentList; //agents injected that I can instance
    private Map<String, AgentInterface> agentList; //agent instances I have created

    private Map<String, Behaviour> availableBehaviourList; //behaviours I can instance and attach to agents via API
    private List<Behaviour> behaviourList; //behaviour instances
    /**local variables above**/

    @PostConstruct
    public void startup(){
        /**Add below those agents you have injected above. These instances will be used as model to create others**/
        //add agents to availableAgentList
        availableAgentList = new HashMap<>();
//        availableAgentList.put(helloAgent.getNickname(), helloAgent);
//        availableAgentList.put(byeAgent.getNickname(), byeAgent);
//        availableAgentList.put(receiveMessageAgent.getNickname(), receiveMessageAgent);
//        availableAgentList.put(sendMessageAgent.getNickname(), sendMessageAgent);
        availableAgentList.put(helloAgent.getAgentClassName(), helloAgent);
        availableAgentList.put(byeAgent.getAgentClassName(), byeAgent);
        availableAgentList.put(receiveMessageAgent.getAgentClassName(), receiveMessageAgent);
        availableAgentList.put(sendMessageAgent.getAgentClassName(), sendMessageAgent);


        /**create agents instances below**/
        agentList = new HashMap<>();
        AgentInterface secondHelloAgent =((SpringAgent)helloAgent).getNewInstance();
        secondHelloAgent.setNickname("SecondHelloAgent");
        agentList.put(secondHelloAgent.getNickname(), secondHelloAgent);

        agentList.put(sendMessageAgent.getNickname(), sendMessageAgent);


        /**Add below those customized behaviours you have injected above**/
        availableBehaviourList = new HashMap<>();
        availableBehaviourList.put(sendACLMessageBlockBehaviour.getBehaviourName(), sendACLMessageBlockBehaviour);
        availableBehaviourList.put(receiveACLMessageBlockBehaviour.getBehaviourName(), receiveACLMessageBlockBehaviour);

        /**create and bound behaviours to agents below**/

        behaviourList = new ArrayList<>();
        //example of behaviour created and set from controller using simpleBehaviourSpring (a factory class)
        SpringBehaviour.ActionInterface actionInterface =
                () -> System.out.println("injecting behaviour and setting one shot");


        Behaviour oneShotBehaviour = simpleBehaviourSpring.addOneShotBehaviour(actionInterface, secondHelloAgent);
        oneShotBehaviour.setBehaviourName("OneShotBehaviour");
        behaviourList.add(oneShotBehaviour);

        SpringBehaviour.ActionInterface actionInterfaceCyclic =
                () -> System.out.println("injecting behaviour and setting cyclic behaviour. FOR EVER :)");
        Behaviour cyclicBehaviour = simpleBehaviourSpring.addCyclicBehaviour(actionInterfaceCyclic, helloAgent);
        cyclicBehaviour.setBehaviourName("CyclicBehaviour");
        behaviourList.add(cyclicBehaviour);

        //example of behaviour template created in examples; you only need to attach the agent to the behaviour
        //on SendACLMessageBlockBehaviour you also need to attach the receiver name
        //Do I need to inject it actually? getInstance..is static method
        ReceiveACLMessageBlockBehaviour receiveBehaviour = (ReceiveACLMessageBlockBehaviour)receiveACLMessageBlockBehaviour.getInstance(receiveMessageAgent.getAgentInstance());
        receiveMessageAgent.addBehaviourToAgent(receiveBehaviour);
//        behaviourList.put(receiveBehaviour.getBehaviourName(), receiveBehaviour);
        behaviourList.add(receiveBehaviour);

        SendACLMessageBlockBehaviour sendBehaviour = (SendACLMessageBlockBehaviour)sendACLMessageBlockBehaviour.getInstance(sendMessageAgent.getAgentInstance());
        sendBehaviour.setReceiverLocalName(receiveMessageAgent.getNickname());
        sendBehaviour.setLanguage("Spanish");
        sendMessageAgent.addBehaviourToAgent(sendBehaviour);
//        behaviourList.put(sendBehaviour.getBehaviourName(), sendBehaviour);
        behaviourList.add(sendBehaviour);

        //Todo: delete this last behaviour
        ReceiveACLMessageBlockBehaviour receiveBehaviour2 = (ReceiveACLMessageBlockBehaviour)receiveACLMessageBlockBehaviour.getInstance(byeAgent.getAgentInstance());
        byeAgent.addBehaviourToAgent(receiveBehaviour2);
//        behaviourList.put(receiveBehaviour.getBehaviourName(), receiveBehaviour);
        behaviourList.add(receiveBehaviour2);
    }

    @GetMapping
    public List<APIActionDescription> getApiDescription(){
        return apiDescriptionService.getAPIDescription();
    }

    @GetMapping("/agent")
    public ResponseMessageInterface getAgentFrontpage(){
        return new ResponseNotificationMessage("go to /api to see the API description. Use /api/agent/{id} to get your agent data. Use /api/agents to get all active agents");
    }

    @GetMapping("/agent/{localName}")
    public Agent getAgentByLocalNameOnContainer(@PathVariable("localName") String localName){
        return apiAgentService.findActiveAgentByLocalName(localName);
    }

    @GetMapping("/agent/available/{className}")
    public Agent getAvailableAgentByLocalName(@PathVariable("className") String className){
        return availableAgentList.get(className).getAgentInstance();
    }

    @GetMapping("/agent/created/{localName}")
    public Agent getCreatedAgentByLocalName(@PathVariable("localName") String localName){
        return agentList.get(localName).getAgentInstance();
    }

    @GetMapping("/agent/description/{localName}")
    public AMSAgentDescription getAgentDescriptionByLocalName(@PathVariable("localName") String localName){
        AMSAgentDescription agentDescription = amsAgent.getActiveAgentByLocalName(localName);
        if (agentDescription == null){
            agentDescription = new AMSAgentDescription();
        }
        return agentDescription;
    }

    @GetMapping("/agent/state/{localName}")
    public ResponseEntity<?> getAgentStateOnContainerByLocalName(@PathVariable("localName") String localName){
        try {
        AgentInterface agentToCheck = agentList.get(localName);
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(localName);
        if (agentToCheck != null){
            if (agentRunningOnContainer != null){
                //todo migrate this to API Service
                State state = ((SpringAgent)agentRunningOnContainer).getContainerController().getAgent(localName).getState();
                String name = state.getName();
                int code = state.getCode();
                return ResponseEntity.ok(new JsonStateAgentModel(name, code));
            }else{
                return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + localName + " hasn't been found running in the system"));
            }
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + localName + " doesn't exist in the system"));
        }
        } catch (ControllerException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ResponseErrorMessage("Seems to have been a problem. Try again later."));
        }
    }

    @GetMapping("/agent/available/state/{localName}")
    public ResponseEntity<?> getAgentStateByLocalName(@PathVariable("localName") String localName) {
        AgentInterface agentToCheck = agentList.get(localName);
        if (agentToCheck != null) {
            AgentState agentState = agentToCheck.getAgentInstance().getAgentState();
            String state = agentState.getName();
            int code = agentState.getValue();
            return ResponseEntity.ok(new JsonStateAgentModel(state, code));
        } else {
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + localName + " doesn't exist in the system"));
        }
    }

    @GetMapping("/agents")
    public List<Agent> getAgents(){
        return apiAgentService.findActiveAgents();
    }

    @GetMapping("/agents/names")
    public List<String> getAgentsName(){
        return apiAgentService.findActiveAgentsName();
    }

    @GetMapping("/agents/description")
    public List<AMSAgentDescription> getAgentsDescription(){
        return amsAgent.getActiveAgentList(false);
    }

//    @GetMapping("/agents/all")  //not working at the moment, I need to get df and ams too from container ...
//    public List<Agent> getAllAgents(){
//        return agentsManager.getAgentsOnContainer();
//    }

    @GetMapping("/agents/all/description")
    public List<AMSAgentDescription> getAllAgentsDescription(){
        return amsAgent.getActiveAgentList(true);
    }

    @GetMapping("/agents/available") //agents you can work with. You can use them to create instances (Agents injected)
    public List<AgentInterface> getAvailableAgents(){
        return new ArrayList<>(availableAgentList.values());
    }

    @GetMapping("/agents/available/names") //agents you can work with. You can use them to create instances (Agents injected)
    public List<String> getAvailableAgentsName(){
        return new ArrayList<>(availableAgentList.keySet());
    }

    @GetMapping("/agents/created") //agents you have instantiate and can manage
    public List<AgentInterface> getCreatedAgents(){
        return new ArrayList<>(agentList.values());
    }

    @GetMapping("/agents/created/names") //agents you have instantiate and can manage
    public List<String> getCreatedAgentsName(){
        return new ArrayList<>(agentList.keySet());
    }

    //TODO TESTINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
    @GetMapping("/agents/available/behaviours")
    public List<JsonBehavioursModel> getBehavioursFromAllAgent(){
        return this.getBehavioursFromAllAgentsInjected();
    }

//    @GetMapping("/agents/available/behaviours")
//    public Map<String, List<Behaviour>> getBehavioursFromAllAgent(){
//        return this.getBehavioursFromAllAgentsInjected();
//    }

//    @GetMapping("/agents/available/behaviours")
//    public List<AbstractMap.SimpleEntry<Agent, List<Behaviour>>> getBehavioursFromAllAgent(){
//        return this.getBehavioursFromAllAgentsInjected();
//    }

    //TODO ABOVE METHOD IS BEING TESTING -> AFTER THAT ADD TO API

    @GetMapping("/agent/available/behaviours/{localName}")
    public List<Behaviour> getBehavioursFromAgent(@PathVariable("localName") String localName){
        return this.getBehavioursFromAgentInjectedByLocalName(localName);
    }

    @GetMapping("/agent/available/behaviours/names/{localName}")
    public List<String> getBehavioursNameFromAgent(@PathVariable("localName") String localName){
        return this.getBehavioursNameFromAgentInjectedByLocalName(localName);
    }

    //container may have other agents that are not controlled from API
    @GetMapping("/agent/behaviours/{localName}")
    public List<Behaviour> getBehavioursFromAgentOnContainer(@PathVariable("localName") String localName){
        return apiAgentService.findBehavioursFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/names/{localName}")
    public List<String> getBehavioursNameFromAgentOnContainer(@PathVariable("localName") String localName){
        return apiAgentService.findBehavioursNameFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/ready/{localName}")
    public List<Behaviour> getReadyBehavioursFromAgentOnContainer(@PathVariable("localName") String localName){
        return apiAgentService.findReadyBehavioursFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/ready/names/{localName}")
    public List<String> getReadyBehavioursNameFromAgentOnContainer(@PathVariable("localName") String localName){
        return apiAgentService.findReadyBehavioursNameFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/blocked/{localName}")
    public List<Behaviour> getBlockedBehavioursFromAgentOnContainer(@PathVariable("localName") String localName){
        return apiAgentService.findBlockedBehavioursFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/blocked/names/{localName}")
    public List<String> getBlockedBehavioursNameFromAgentOnContainer(@PathVariable("localName") String localName){
        return apiAgentService.findBlockedBehavioursNameFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/running/{localName}")
    public List<Behaviour> getRunningBehavioursFromAgentOnContainer(@PathVariable("localName") String localName){
        return apiAgentService.findRunningBehavioursFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/running/names/{localName}")
    public List<String> getRunningBehavioursNameFromAgentOnContainer(@PathVariable("localName") String localName){
        return apiAgentService.findRunningBehavioursNameFromAgentByLocalName(localName);
    }

    @PostMapping(path="agent/init", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initAgent(@RequestBody JsonAgentBehaviourModel jsonAgentBehaviourModel){
        String agentName = jsonAgentBehaviourModel.getAgentName();
        AgentInterface agentToInit = agentList.get(agentName);
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(agentName);
        if (agentToInit != null){
           if (agentRunningOnContainer == null){
               ((SpringAgent) agentToInit.getAgentInstance()).init();
               return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + agentName + " initiated successfully"));
           }else{
               return ResponseEntity.status(409).body(new ResponseNotificationMessage("Agent " + agentName + " was already running. You can't init the same agent twice"));
           }
        }else if (agentName == null){
            return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Value {'agentName':'<your agent nickname/localName>'} is required"));
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + agentName + " doesn't exist in the system"));
        }
    }

    @PostMapping(path="agent/stop", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stopAgent(@RequestBody JsonAgentBehaviourModel jsonAgentBehaviourModel){
        String agentName = jsonAgentBehaviourModel.getAgentName();
        AgentInterface agentToStop = agentList.get(agentName);
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(agentName);
        AgentInterface newAgentInstance;
            if (agentToStop != null){
                if (agentRunningOnContainer != null){
                    newAgentInstance =  ((SpringAgent) agentRunningOnContainer).shutDownAgent();
                    //add the new instance of this agent to agentList, overriding the old instance we have just stopped
                    agentList.put(agentName, newAgentInstance);
                    return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + agentName) + " stopped successfully");
                }else{
                    return ResponseEntity.status(409).body(new ResponseNotificationMessage("Agent " + agentName + " hasn't been found running in the system"));
                }
            }else if (agentName == null){
                return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Value {'agentName':'<your agent nickname/localName>'} is required"));
            }else{
                return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + agentName + " doesn't exist in the system"));
            }
        }

    @PostMapping(path="agent/restart", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restartAgent(@RequestBody JsonAgentBehaviourModel jsonAgentBehaviourModel){
        String agentName = jsonAgentBehaviourModel.getAgentName();
        AgentInterface agentToRestart = agentList.get(agentName);
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(agentName);
        AgentInterface newAgentInstance;
        if (agentToRestart != null){
            if (agentRunningOnContainer != null){
                newAgentInstance = ((SpringAgent) agentRunningOnContainer).shutDownAgent();
                //add the new instance of this agent to agentList, overriding the old instance we have just stopped
                agentList.put(agentName, newAgentInstance);
                try {
                    Thread.sleep(1000);
                while (((SpringAgent) agentRunningOnContainer).isInitiated()){
                    //waiting until agent be shut down
                    Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                newAgentInstance.init();
                return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + agentName + " was restarted successfully"));
            }else{
                return ResponseEntity.status(409).body(new ResponseNotificationMessage("Agent " + agentName + " hasn't been found running in the system"));
            }
        }else if (agentName == null){
            return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Value {'agentName':'<your agent nickname/localName>'} is required"));
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + agentName + " doesn't exist in the system"));
        }
    }

    @PostMapping(path="agent/create", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAgent(@RequestBody JsonAgentBehaviourModel jsonAgentBehaviourModel){
        String className = jsonAgentBehaviourModel.getAgentClassName();
        String agentName = jsonAgentBehaviourModel.getAgentName();
        AgentInterface agentToCreateInstance;
        AgentInterface newAgent;
        if (checkAgentClassExistsByClassName(className)){
            if (checkAgentInstanceExistsByName(agentName)){
                agentToCreateInstance = availableAgentList.get(className);
                newAgent = ((SpringAgent) agentToCreateInstance).getNewInstance();
                newAgent.setNickname(agentName);
                agentList.put(agentName, newAgent);
                return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + agentName + " from class " + className + " has been created successfully"));
            }else{
                return ResponseEntity.status(409).body(new ResponseNotificationMessage("A agent with  " + jsonAgentBehaviourModel.getAgentName() + " exists already. You can't create two agents with the same name"));
            }
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent class " + jsonAgentBehaviourModel.getAgentClassName() + " doesn't exist in the system"));
        }
    }

    @PostMapping(path="agent/delete", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAgent(@RequestBody JsonAgentBehaviourModel jsonAgentBehaviourModel){
        String agentName = jsonAgentBehaviourModel.getAgentName();
        AgentInterface agentToDelete;
            if (checkAgentInstanceExistsByName(agentName)){
                agentToDelete = agentList.get(agentName);
                if (((SpringAgent)agentToDelete).isInitiated()){
                    agentToDelete.shutDownAgent();
                }
                agentList.remove(agentName);
                return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + agentName + " has been deleted successfully"));
            }else{
                return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + agentName + " doesn't exist in the system"));
            }
    }


    @GetMapping("/behaviours") //behaviours instances bounded to agents.
    public List<Behaviour> getBehaviours(){
        List<Behaviour> result = new ArrayList<>(behaviourList);

        for (Behaviour behaviour : result){
            behaviour.setAgent(null);
        }
        return result;
    }

    @GetMapping("/behaviours/names")
    public List<String> getBehavioursName(){
        List<String> result = new ArrayList<>();
        for (Behaviour behaviour : behaviourList){
            result.add(behaviour.getBehaviourName());
        }
        return result;
    }

    @GetMapping("/behaviours/available") //Behaviours injected
    public List<Behaviour> getAvailableBehaviours(){
        return new ArrayList<>(availableBehaviourList.values());
    }

    @GetMapping("/behaviours/available/names")
    public List<String> getAvailableBehavioursName(){
        return new ArrayList<>(availableBehaviourList.keySet());
    }

    @PostMapping(path="agent/add/behaviour", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addBehaviourToAgent(@RequestBody JsonAgentBehaviourModel jsonAgentBehaviourModel){
        boolean isBehaviourAdded;
        String agentName = jsonAgentBehaviourModel.getAgentName();
        String behaviourName = jsonAgentBehaviourModel.getBehaviourName();
        boolean startNow = jsonAgentBehaviourModel.getStartNow();

        AgentInterface agent = agentList.get(agentName);
        Behaviour behaviour = availableBehaviourList.get(behaviourName);

        if (agent != null && behaviour != null){
            if (behaviour instanceof BehaviourWithFactoryInterface){
                behaviour = ((BehaviourWithFactoryInterface) behaviour).getInstance(agent.getAgentInstance());
            }else if (behaviour.getAgent() != null){
                return ResponseEntity.badRequest().body(new ResponseErrorMessage("Behaviour " + behaviourName
                + " is already attached to the Agent " + behaviour.getAgent().getLocalName() + ". You should use behaviours" +
                        " with factory methods to get multiples instances for different agents."));
            }

//            if (jsonAgentBehaviourModel.getStartNow() == null || !jsonAgentBehaviourModel.getStartNow()) {
            if (!startNow) {
                isBehaviourAdded = agent.addBehaviourToAgent(behaviour);
            }else{
                isBehaviourAdded = agent.addBehaviourToAgentAndInit(behaviour);
            }

            if (isBehaviourAdded){
                behaviourList.add(behaviour);
                return ResponseEntity.ok(new ResponseNotificationMessage("Behaviour" + behaviourName
                        + " has been added to the agent " + agentName + " successfully"));
            }else{
                return ResponseEntity.status(500).body(new ResponseErrorMessage("The agent " + agentName
                        + " has already the behaviour " + behaviourName + ". It is not allowed" +
                        " to add a behaviour to the same agent twice"));
            }

        }else if (agentName == null || behaviourName == null){
            return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Values 'agentName' and 'behaviourName' are required"));
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + agentName + " or behaviour "
                    + behaviourName + " don't exist in the system"));
        }

    }


    @PostMapping(path="agent/reset/behaviour", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetBehaviourFromAgent(@RequestBody JsonAgentBehaviourModel jsonAgentBehaviourModel){
        boolean isBehaviourReset;
        String agentName = jsonAgentBehaviourModel.getAgentName();
        String behaviourName = jsonAgentBehaviourModel.getBehaviourName();

        AgentInterface agent = agentList.get(agentName);
        //We could've avoid this step but we want to be able of response that the behaviour specified don't exist in
        //the system instead of only saying that is not attached to the agent
        boolean behaviourExists = checkBehaviourExistsByName(behaviourName);

        if (agent != null && behaviourExists){
            isBehaviourReset = agent.resetBehaviourFromAgentByName(behaviourName);

            if (isBehaviourReset){
                return ResponseEntity.ok(new ResponseNotificationMessage("Behaviour " + behaviourName
                        + " on agent " + agentName + " has been reset successfully"));
            }else{
                return ResponseEntity.status(500).body(new ResponseErrorMessage("Behaviour " + behaviourName
                        + " is not attached to the agent " + agentName +
                        " or it is not added to the current execution"));
            }
        }else if (agentName == null || behaviourName == null){
            return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Values 'agentName' and 'behaviourName' are required"));
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + agentName + " or behaviour "
                    + behaviourName + " don't exist in the system"));
        }

    }

    @PostMapping(path="agent/remove/behaviour", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeBehaviourFromAgent(@RequestBody JsonAgentBehaviourModel jsonAgentBehaviourModel){
        boolean isBehaviourRemoved;
        String agentName = jsonAgentBehaviourModel.getAgentName();
        String behaviourName = jsonAgentBehaviourModel.getBehaviourName();
        boolean removeForever = jsonAgentBehaviourModel.getForever();

        AgentInterface agent = agentList.get(agentName);
        Behaviour behaviourToRemove;
        String successMessage;

        if (agent != null){
            behaviourToRemove = getBehaviourByNameAndAgent(agent.getAgentInstance(), behaviourName);
            if (behaviourToRemove != null) {
//                removeForever = jsonAgentBehaviourModel.getForever() == null ? false : jsonAgentBehaviourModel.getForever();
                if (removeForever) {
                    isBehaviourRemoved = agent.removeBehaviourFromAgentForever(behaviourToRemove);
                }else{
                    isBehaviourRemoved = agent.removeBehaviourFromAgent(behaviourToRemove);
                }

                if (isBehaviourRemoved) {
                    successMessage = "Behaviour " + behaviourName
                            + " on agent " + agentName + " has been removed successfully";

                    if (removeForever){
                        //only remove behaviour from list if it is removed forever. Otherwise for current run
                        //the behaviour is not running but we can not delete it from the list todo may the problem of checking behaviours
                        //and see one that is not working on in the current run
                        behaviourList.remove(behaviourToRemove);
                        successMessage += " forever";
                    }
                    return ResponseEntity.ok(new ResponseNotificationMessage(successMessage));
                } else {
                    //we should never reach this response.
                    return ResponseEntity.status(500).body(new ResponseErrorMessage("It seems we have had some problems" +
                            " removing the behaviour " + behaviourName
                            + ". Please, try again later"));
                }
            }else if (behaviourName == null){
                return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Value 'behaviourName' is required"));
            }else{
                return ResponseEntity.badRequest().body(new ResponseErrorMessage("Behaviour " + behaviourName
                        + " don't exist attached to agent " + agentName + " in the system"));
            }
        }else if (agentName == null){
            return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Value 'agentName' is required"));
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + agentName
                    + " don't exist in the system"));
        }

    }

    private List<Behaviour> getBehavioursFromAgentInjectedByLocalName(String localName){
        List<Behaviour> result = new ArrayList<>();
        AgentInterface agent = agentList.get(localName);
        if (agent != null){
            result = new ArrayList<>(agent.getBehavioursFromAgent());
            for (Behaviour behaviour : result){
                behaviour.setAgent(null);
            }
        }
        return result;
    }

    private List<String> getBehavioursNameFromAgentInjectedByLocalName(String localName){
        List<String> result = new ArrayList<>();
        List<Behaviour> behaviourList = getBehavioursFromAgentInjectedByLocalName(localName);
        for (Behaviour behaviour : behaviourList){
            result.add(behaviour.getBehaviourName());
        }
        return result;
    }

    private List<JsonBehavioursModel> getBehavioursFromAllAgentsInjected(){
        List<JsonBehavioursModel> result = new ArrayList<>();
        String agentName;

        for (AgentInterface agentInterface : agentList.values()){
            agentName = agentInterface.getNickname();
            result.add(new JsonBehavioursModel(agentName, getBehavioursFromAgentInjectedByLocalName(agentName)));
        }
        return result;
    }

//    private Map<String, List<Behaviour>> getBehavioursFromAllAgentsInjected(){
//        Map<String, List<Behaviour>> result = new HashMap<>();
//
//        for (AgentInterface agentInterface : agentList.values()){
//            result.put(agentInterface.getNickname(), getBehavioursFromAgentInjectedByLocalName(agentInterface.getNickname()));
//        }
//        return result;
//    }

//    private List<AbstractMap.SimpleEntry<Agent, List<Behaviour>>> getBehavioursFromAllAgentsInjected(){
//        List<AbstractMap.SimpleEntry<Agent, List<Behaviour>>> result = new ArrayList<>();
//
//        for (AgentInterface agentInterface : agentList.values()){
//            result.add(new AbstractMap.SimpleEntry<>(agentInterface.getAgentInstance(), getBehavioursFromAgentInjectedByLocalName(agentInterface.getNickname())));
//        }
//        return result;
//    }

    private Boolean checkBehaviourExistsByName(String behaviourName){
        boolean behaviourFound = false;
        int index = 0;
        String behaviourNameInList;

        if (behaviourName != null) {
            while (!behaviourFound && index < behaviourList.size()) {
                behaviourNameInList = behaviourList.get(index).getBehaviourName();

                if (behaviourNameInList != null && behaviourNameInList.equals(behaviourName)) {
                    behaviourFound = true;
                } else {
                    index++;
                }
            }
        }

        return behaviourFound;
    }


    private Behaviour getBehaviourByNameAndAgent(Agent agent, String behaviourName){
        Behaviour behaviourFound = null;
        int index = 0;
        String behaviourNameInList;
        Agent agentInList;

        if (agent != null && behaviourName != null){
            while (behaviourFound == null && index < behaviourList.size()){
                behaviourNameInList = behaviourList.get(index).getBehaviourName();
                agentInList = behaviourList.get(index).getAgent();
                if (behaviourNameInList != null && behaviourName.equals(behaviourNameInList) && agent.equals(agentInList)){
                    behaviourFound = behaviourList.get(index);
                }else{
                    index++;
                }
            }
        }
        return behaviourFound;
    }

    private Boolean checkAgentClassExistsByClassName(String className){
        return availableAgentList.containsKey(className);
    }

    private Boolean checkAgentInstanceExistsByName(String nickname){
        return agentList.containsKey(nickname);
    }


    //get behaviours

    //not working, cycle reference don't know why
//    @GetMapping("/agents/all2")
//    public List<Agent> getAllAgents2(){
//        return agentsManager.getAgentsOnContainer();
////        return amsAgent.agentsIn
//    }


//    @GetMapping(value = "/{id}")
//    public Student getStudentById(@PathVariable("id") int id){
//        return studentService.getStudentById(id);
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public void deleteStudentById(@PathVariable("id") int id){
//        studentService.removeStudentById(id);
//    }
//
//    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public void deleteStudentById(@RequestBody Student student){
//        studentService.updateStudent(student);
//    }
//
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public void insertStudent(@RequestBody Student student){
//        studentService.insertStudent(student);
//    }


}
