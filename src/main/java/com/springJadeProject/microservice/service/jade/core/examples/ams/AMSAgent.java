package com.springJadeProject.microservice.service.jade.core.examples.ams;

import com.springJadeProject.microservice.service.jade.core.agent.AgentInterface;
import com.springJadeProject.microservice.service.jade.core.agent.AgentSpring;
import jade.core.AID;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alejandro Reyes
 */


//@Stateless @AgentQualifier(type=AgentType.AMSAGENT)
@Component
public class AMSAgent extends AgentSpring implements AgentInterface {
    private static final String AMS_LOCAL_NAME = "ams";
    private static final String DF_LOCAL_NAME = "df";
    private static final String RMA_LOCAL_NAME = "rma";

    private boolean initiated = false;

    public boolean isInitiated (){
        return initiated;
    }

    @Override
    protected void setup()
    {
        initiated = true;
        super.setup();
        System.out.println("The agent " + getAID().getName() + " has been initiated.");
    }

    public List<AMSAgentDescription> getActiveAgentList(boolean allAgents) {
        AMSAgentDescription [] agents = {};
        List<AMSAgentDescription> customAgents = new ArrayList<>();
        try
        {
            SearchConstraints restrictions = new SearchConstraints();
            restrictions.setMaxResults ( -1L); /// All of them
            //new AMSAgentDescription because we don't need any concrete value for any field of AgentDescription
            agents = AMSService.search(this, new AMSAgentDescription(), restrictions);
        }
        catch (Exception e) { System.out.println(e); }


        for (int i=0; i < agents.length; i++)
        {
            AID id = agents[i].getName();
            String agentLocalName = id.getLocalName();
            if (allAgents ||(!agentLocalName.equals(AMS_LOCAL_NAME) && !agentLocalName.equals(DF_LOCAL_NAME) && !agentLocalName.equals(RMA_LOCAL_NAME))){
                customAgents.add(agents[i]);
            }
        }
        return customAgents;
    }

    @Override
    protected void takeDown() {
        System.out.println("---->AMS Agent: getAID.getName --> " + this.getAID().getName() + " terminating");
    }

    @Override
    protected AgentInterface getNewInstance() {
        initiated  = false;
        return new AMSAgent();
    }

}

