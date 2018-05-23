package com.springJadeProject.microservice.controller;

import com.springJadeProject.microservice.model.AgentModel.JsonNicknameAgentModel;
import com.springJadeProject.microservice.model.AgentModel.JsonStateAgentModel;
import com.springJadeProject.microservice.model.ResponseMessageModel.APIActionDescription;
import com.springJadeProject.microservice.model.ResponseMessageModel.ResponseErrorMessage;
import com.springJadeProject.microservice.model.ResponseMessageModel.ResponseNotificationMessage;
import com.springJadeProject.microservice.model.ResponseMessageModel.contract.ResponseMessageInterface;
import com.springJadeProject.microservice.service.api.APIAgentService;
import com.springJadeProject.microservice.service.api.APIDescriptionService;
import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.core.agent.SpringAgent;
import com.springJadeProject.microservice.service.jade.core.behaviour.SimpleBehaviourSpring;
import com.springJadeProject.microservice.service.jade.core.behaviour.SpringBehaviour;
import com.springJadeProject.microservice.service.jade.core.examples.behaviour.ReceiveACLMessageBlockBehaviour;
import com.springJadeProject.microservice.service.jade.core.examples.behaviour.SendACLMessageBlockBehaviour;
import com.springJadeProject.microservice.service.jade.core.manager.AMSAgent;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.wrapper.ControllerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//    private List<Agent> availableAgentList;
//    private Map<String,Agent> availableAgentList;
    private Map<String,AgentInterface> availableAgentList;
    //behaviours I can instance and attach to agents via API
    private Map<String,Behaviour> availableBehaviourList;
    private List<Behaviour> behaviourList;
    /**local variables above**/

    @PostConstruct
    public void startup(){
        /**Add below those agents you have injected above**/
        //add agents to availableAgentList
        availableAgentList = new HashMap<>();
        availableAgentList.put(helloAgent.getNickname(), helloAgent);
        availableAgentList.put(byeAgent.getNickname(), byeAgent);
        availableAgentList.put(receiveMessageAgent.getNickname(), receiveMessageAgent);
        availableAgentList.put(sendMessageAgent.getNickname(), sendMessageAgent);

        /**Add below those customized behaviours you have injected above**/
        availableBehaviourList = new HashMap<>();
        availableBehaviourList.put(sendACLMessageBlockBehaviour.getBehaviourName(), sendACLMessageBlockBehaviour);
        availableBehaviourList.put(receiveACLMessageBlockBehaviour.getBehaviourName(), receiveACLMessageBlockBehaviour);

        /**create and bound behaviours to agents below**/
//        behaviourList = new HashMap<>();
        behaviourList = new ArrayList<>();
        //example of behaviour created and set from controller using simpleBehaviourSpring (a factory class)
        SpringBehaviour.ActionInterface actionInterface =
                () -> System.out.println("injecting behaviour and setting one shot");


        Behaviour oneShotBehaviour = simpleBehaviourSpring.addOneShotBehaviour(actionInterface, helloAgent);
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
        ReceiveACLMessageBlockBehaviour receiveBehaviour = receiveACLMessageBlockBehaviour.getInstance(receiveMessageAgent.getAgentInstance());
        receiveMessageAgent.addBehaviourToAgent(receiveBehaviour);
//        behaviourList.put(receiveBehaviour.getBehaviourName(), receiveBehaviour);
        behaviourList.add(receiveBehaviour);

        SendACLMessageBlockBehaviour sendBehaviour = sendACLMessageBlockBehaviour.getInstance(sendMessageAgent.getAgentInstance());
        sendBehaviour.setReceiverLocalName(receiveMessageAgent.getNickname());
        sendBehaviour.setLanguage("Spanish");
        sendMessageAgent.addBehaviourToAgent(sendBehaviour);
//        behaviourList.put(sendBehaviour.getBehaviourName(), sendBehaviour);
        behaviourList.add(sendBehaviour);

        //Todo: delete this last behaviour
        ReceiveACLMessageBlockBehaviour receiveBehaviour2 = receiveACLMessageBlockBehaviour.getInstance(byeAgent.getAgentInstance());
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
        return new ResponseNotificationMessage("use /api/agent/{id} to get your agent data. Use /api/agents to get all active agents");
    }

    @GetMapping("/agent/{localName}")
    public Agent getAgentByLocalName(@PathVariable("localName") String localName){
        return apiAgentService.findActiveAgentByLocalName(localName);
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
    public ResponseEntity<?> getAgentDescriptionById(@PathVariable("localName") String localName){
        try {
        AgentInterface agentToCheck = availableAgentList.get(localName);
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(localName);
        if (agentToCheck != null){
            if (agentRunningOnContainer != null){
                String state = ((SpringAgent)agentRunningOnContainer).getContainerController().getAgent(localName).getState().toString();
                return ResponseEntity.ok(new JsonStateAgentModel(state));
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

    @GetMapping("/agents")
    public List<Agent> getAgents(){
        return apiAgentService.findActiveAgents();
    }

    @GetMapping("/agents/name")
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

    @GetMapping("/agents/available") //agents you can work with. (Agents injected)
    public List<AgentInterface> getAvailableAgents(){
        return new ArrayList<>(availableAgentList.values());
    }

    @GetMapping("/agents/available/names") //agents you can work with. (Agents injected)
    public List<String> getAvailableAgentsName(){
        return new ArrayList<>(availableAgentList.keySet());
    }

    @GetMapping("/agent/behaviours/{localName}")
    public List<Behaviour> getBehavioursFromAgent(@PathVariable("localName") String localName){
        return apiAgentService.findBehavioursFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/names/{localName}")
    public List<String> getBehavioursNameFromAgent(@PathVariable("localName") String localName){
        return apiAgentService.findBehavioursNameFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/ready/{localName}")
    public List<Behaviour> getReadyBehavioursFromAgent(@PathVariable("localName") String localName){
        return apiAgentService.findReadyBehavioursFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/ready/names/{localName}")
    public List<String> getReadyBehavioursNameFromAgent(@PathVariable("localName") String localName){
        return apiAgentService.findReadyBehavioursNameFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/blocked/{localName}")
    public List<Behaviour> getBlockedBehavioursFromAgent(@PathVariable("localName") String localName){
        return apiAgentService.findBlockedBehavioursFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/blocked/names/{localName}")
    public List<String> getBlockedBehavioursNameFromAgent(@PathVariable("localName") String localName){
        return apiAgentService.findBlockedBehavioursNameFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/running/{localName}")
    public List<Behaviour> getRunningBehavioursFromAgent(@PathVariable("localName") String localName){
        return apiAgentService.findRunningBehavioursFromAgentByLocalName(localName);
    }

    @GetMapping("/agent/behaviours/running/names/{localName}")
    public List<String> getRunningBehavioursNameFromAgent(@PathVariable("localName") String localName){
        return apiAgentService.findRunningBehavioursNameFromAgentByLocalName(localName);
    }

    @PostMapping(path="agent/init", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initAgent(@RequestBody JsonNicknameAgentModel jsonNicknameAgentModel){
        AgentInterface agentToInit = availableAgentList.get(jsonNicknameAgentModel.getNickname());
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(jsonNicknameAgentModel.getNickname());
        if (agentToInit != null){
           if (agentRunningOnContainer == null){
               ((SpringAgent) agentToInit.getAgentInstance()).init();
               return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " initiated successfully"));
           }else{
               return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " was already running. You can't init the same agent twice"));
           }
        }else if (jsonNicknameAgentModel.getNickname() == null){
            return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Value {'nickname':'<your agent nickname>'} is required"));
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + jsonNicknameAgentModel.getNickname() + " doesn't exist in the system"));
        }
    }

    @PostMapping(path="agent/stop", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stopAgent(@RequestBody JsonNicknameAgentModel jsonNicknameAgentModel){
        AgentInterface agentToStop = availableAgentList.get(jsonNicknameAgentModel.getNickname());
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(jsonNicknameAgentModel.getNickname());
            if (agentToStop != null){
                if (agentRunningOnContainer != null){
                    AgentInterface newAgentInstance =  ((SpringAgent) agentRunningOnContainer).shutDownAgent();
                    //add the new instance of this agent to availableAgentList
                    availableAgentList.put(jsonNicknameAgentModel.getNickname(), newAgentInstance);
                    return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " stopped successfully"));
                }else{
                    return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " hasn't been found running in the system"));
                }
            }else if (jsonNicknameAgentModel.getNickname() == null){
                return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Value {'nickname':'<your agent nickname>'} is required"));
            }else{
                return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + jsonNicknameAgentModel.getNickname() + " doesn't exist in the system"));
            }
        }

    @PostMapping(path="agent/restart", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restartAgent(@RequestBody JsonNicknameAgentModel jsonNicknameAgentModel){
        AgentInterface agentToRestart = availableAgentList.get(jsonNicknameAgentModel.getNickname());
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(jsonNicknameAgentModel.getNickname());
        if (agentToRestart != null){
            if (agentRunningOnContainer != null){
                AgentInterface newAgentInstance = ((SpringAgent) agentRunningOnContainer).shutDownAgent();
                //add the new instance of this agent to availableAgentList
                availableAgentList.put(jsonNicknameAgentModel.getNickname(), newAgentInstance);
                try {
                while (((SpringAgent) agentRunningOnContainer).isInitiated()){
                    //waiting until agent be shut down
                    Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                newAgentInstance.init();
                return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " was restarted successfully"));
            }else{
                return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " hasn't been found running in the system"));
            }
        }else if (jsonNicknameAgentModel.getNickname() == null){
            return ResponseEntity.unprocessableEntity().body(new ResponseErrorMessage("Value {'nickname':'<your agent nickname>'} is required"));
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + jsonNicknameAgentModel.getNickname() + " doesn't exist in the system"));
        }
    }


    @GetMapping("/behaviours") //behaviours instances bounded to agents.
    public List<Behaviour> getBehaviours(){
        List<Behaviour> result = new ArrayList<>(behaviourList);

        for (Behaviour behaviour : result){
            behaviour.setAgent(null);
        }
        return result;
//        return new ArrayList<>(behaviourList.values());
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

//    @PostMapping("/agent/add/behaviour")
//    @PostMapping("/agent/stop/behaviour")
//    @PostMapping("/agent/restart/behaviour")
//    @PostMapping("/agent/remove/behaviour")
//
//    //todo maybe not all methods??? dunno




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
