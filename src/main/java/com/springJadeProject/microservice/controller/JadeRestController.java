package com.springJadeProject.microservice.controller;

//import com.rest.api.spring.basic.test.v1.model.entity.Student;
//import com.rest.api.spring.basic.test.v1.service.StudentService;
import com.springJadeProject.microservice.model.AgentModel.JsonNicknameAgentModel;
import com.springJadeProject.microservice.model.ResponseMessageModel.APIActionDescription;
import com.springJadeProject.microservice.model.ResponseMessageModel.ResponseErrorMessage;
import com.springJadeProject.microservice.model.ResponseMessageModel.ResponseNotificationMessage;
import com.springJadeProject.microservice.model.ResponseMessageModel.contract.ResponseMessageInterface;
import com.springJadeProject.microservice.service.api.APIAgentService;
import com.springJadeProject.microservice.service.api.APIDescriptionService;
import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.core.agent.AgentSpring;
import com.springJadeProject.microservice.service.jade.core.manager.AMSAgent;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
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
    @Qualifier("ByeAgent")
    private AgentInterface byeAgent;

    @Autowired
    @Qualifier("ReceiveMessageAgent")
    private AgentInterface receiveMessageAgent;

    @Autowired
    @Qualifier("SendMessageAgent")
    private AgentInterface sendMessageAgent;

    /**Inject your agents via @Autowired above*/

    /**Injections required by the framework below**/

    @Autowired
    private APIAgentService apiAgentService;

    @Autowired
    private AMSAgent amsAgent;

    @Autowired
    private APIDescriptionService apiDescriptionService;
    /**Injections required by the framework above**/


//    @Autowired
//    private StudentService studentService;

    /**local variables below**/
//    private List<Agent> availableAgentList;
    private Map<String,Agent> availableAgentList;
    /**local variables above**/

    @PostConstruct
    public void startup(){
        /**Add below those agents you have injected above**/
        availableAgentList = new HashMap<>();
        availableAgentList.put(helloAgent.getNickname(), helloAgent.getAgentInstance());
        availableAgentList.put(byeAgent.getNickname(), byeAgent.getAgentInstance());
        availableAgentList.put(receiveMessageAgent.getNickname(), receiveMessageAgent.getAgentInstance());
        availableAgentList.put(sendMessageAgent.getNickname(), sendMessageAgent.getAgentInstance());
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
    public Agent getAgentById(@PathVariable("localName") String localName){
        return apiAgentService.findActiveAgentByLocalName(localName);
    }

    @GetMapping("/agent/description/{localName}")
    public AMSAgentDescription getAgentDescriptionById(@PathVariable("localName") String localName){
        AMSAgentDescription agentDescription = amsAgent.getActiveAgentByLocalName(localName);
        if (agentDescription == null){
            agentDescription = new AMSAgentDescription();
        }
        return agentDescription;
    }

    @GetMapping("/agents")
    public List<Agent> getAgents(){
        return apiAgentService.findActiveAgents();
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
    public List<Agent> getAvailableAgents(){
        return new ArrayList<>(availableAgentList.values());
    }

    @GetMapping("/agents/available/names") //agents you can work with. (Agents injected)
    public List<String> getAvailableAgentsName(){
        return new ArrayList<>(availableAgentList.keySet());
    }

    @PostMapping(path="agent/init", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> initAgent(@RequestBody JsonNicknameAgentModel jsonNicknameAgentModel){
        Agent agentToInit = availableAgentList.get(jsonNicknameAgentModel.getNickname());
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(jsonNicknameAgentModel.getNickname());
        if (agentToInit != null){
           if (agentRunningOnContainer == null){
               ((AgentSpring) agentToInit).init();
               return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " initiated successfully"));
           }else{
               return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " was already running. You can't init the same agent twice"));
           }
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + jsonNicknameAgentModel.getNickname() + " doesn't exist in the system"));
        }
    }

    @PostMapping(path="agent/stop", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> stopAgent(@RequestBody JsonNicknameAgentModel jsonNicknameAgentModel){
        Agent agentToStop = availableAgentList.get(jsonNicknameAgentModel.getNickname());
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(jsonNicknameAgentModel.getNickname());
            if (agentToStop != null){
                if (agentRunningOnContainer != null){
                    ((AgentSpring) agentRunningOnContainer).shutDownAgent();
                    return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " stopped successfully"));
                }else{
                    return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " hasn't been found running in the system"));
                }
            }else{
                return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + jsonNicknameAgentModel.getNickname() + " doesn't exist in the system"));
            }
        }

    @PostMapping(path="agent/restart", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restartAgent(@RequestBody JsonNicknameAgentModel jsonNicknameAgentModel){
        Agent agentToStop = availableAgentList.get(jsonNicknameAgentModel.getNickname());
        Agent agentRunningOnContainer = apiAgentService.findActiveAgentByLocalName(jsonNicknameAgentModel.getNickname());
        if (agentToStop != null){
            if (agentRunningOnContainer != null){
                ((AgentSpring) agentRunningOnContainer).shutDownAgent();
                ((AgentSpring) agentToStop).init();
                return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " was restarted successfully"));
            }else{
                return ResponseEntity.ok(new ResponseNotificationMessage("Agent " + jsonNicknameAgentModel.getNickname() + " hasn't been found running in the system"));
            }
        }else{
            return ResponseEntity.badRequest().body(new ResponseErrorMessage("Agent " + jsonNicknameAgentModel.getNickname() + " doesn't exist in the system"));
        }
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
