package com.springJadeProject.microservice.controller;

//import com.rest.api.spring.basic.test.v1.model.entity.Student;
//import com.rest.api.spring.basic.test.v1.service.StudentService;
import com.springJadeProject.microservice.service.jade.core.examples.ams.AMSAgent;
import com.springJadeProject.microservice.service.jade.core.manager.AgentsManager;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.wrapper.ControllerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class JadeRestController {

    @Autowired
    private AMSAgent amsAgent;

//    @Autowired
//    private StudentService studentService;

//    @GetMapping
//    public Collection<Student> getAllStudents(){
//        return studentService.getAllStudents();
//    }

    @GetMapping
    public Agent getAgent(){
        return new Agent();
    }

//    @GetMapping
//    @RequestMapping("/load")
//    public Agent load(){
//        amsAgent.init();
//        return new Agent();
//    }

    @GetMapping
    @RequestMapping("/agents")
    public List<AMSAgentDescription> getCustomAgents(){
        if(!amsAgent.isInitiated())amsAgent.init();
        return amsAgent.getActiveAgentList(false);
    }

    @GetMapping
    @RequestMapping("/agents/all")
    public List<AMSAgentDescription> getAllAgents(){
        if(!amsAgent.isInitiated())amsAgent.init();
        return amsAgent.getActiveAgentList(true);
    }

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
