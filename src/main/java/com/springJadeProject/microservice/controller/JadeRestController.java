package com.springJadeProject.microservice.controller;

//import com.rest.api.spring.basic.test.v1.model.entity.Student;
//import com.rest.api.spring.basic.test.v1.service.StudentService;
import com.springJadeProject.microservice.model.ResponseMessageModel.APIActionDescription;
import com.springJadeProject.microservice.model.ResponseMessageModel.ResponseNotificationMessage;
import com.springJadeProject.microservice.model.ResponseMessageModel.contract.ResponseMessageInterface;
import com.springJadeProject.microservice.service.api.APIDescriptionService;
import com.springJadeProject.microservice.service.jade.core.manager.AMSAgent;
import com.springJadeProject.microservice.service.jade.core.manager.AgentsManager;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JadeRestController {

    @Autowired
    private AgentsManager agentsManager;

    @Autowired
    private AMSAgent amsAgent;

    @Autowired
    private APIDescriptionService apiDescriptionService;

//    @Autowired
//    private StudentService studentService;

    @GetMapping
    public List<APIActionDescription> getApiDescription(){
        return apiDescriptionService.getAPIDescription();
    }

    @GetMapping("/agent")
    public ResponseMessageInterface getAgentsFrontpage(){
        return new ResponseNotificationMessage("use /api/agent/{id} to get your agent data. Use /api/agents to get all active agents");
    }

//    @GetMapping("/agent")
//    public Agent getAgentsFrontpage(){
//        return new Agent();
//    }

    @GetMapping("/agent/{localName}")
    public AMSAgentDescription getAgentById(@PathVariable("localName") String localName){
        AMSAgentDescription agent = amsAgent.getActiveAgentByLocalName(localName);
        if (agent == null){
            agent = new AMSAgentDescription();
        }
        return agent;
    }

    @GetMapping("/agents")
    public List<AMSAgentDescription> getAgents(){
        return amsAgent.getActiveAgentList(false);
    }

    @GetMapping("/agents/all")
    public List<AMSAgentDescription> getAllAgents(){
        return amsAgent.getActiveAgentList(true);
    }

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
