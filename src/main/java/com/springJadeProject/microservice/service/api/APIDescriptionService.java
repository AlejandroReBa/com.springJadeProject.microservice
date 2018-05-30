package com.springJadeProject.microservice.service.api;

import com.springJadeProject.microservice.model.responseMessageModel.APIActionDescription;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
public class APIDescriptionService {
    private static final String FILE_NAME = "APIDescription.txt";

//    private static final String actionAPIPath = "/api";
//    private static final String actionAPIMethod = "GET";
//    private static final String actionAPIDescription = "api specification";
//
//    private static final String actionAgentPath = "/api/agent/{localName}";
//    private static final String actionAgentMethod = "GET";
//    private static final String actionAgentDescription = "get information from the specified agent running in the Jade Container";
//
//    private static final String actionAgentsPath = "/api/agents";
//    private static final String actionAgentsMethod = "GET";
//    private static final String actionAgentsDescription = "get information from every agent running in the Jade Container";
//
//    private static final String actionAgentsAllPath = "/api/agents/all";
//    private static final String actionAgentsAllMethod = "GET";
//    private static final String actionAgentsAllDescription = "get information from every agent running in the Jade Container, including the own run by the Jade Platform";

    private List<APIActionDescription> actionList;

    public APIDescriptionService() {
    }

    public List<APIActionDescription> getActionList() {
        return actionList;
    }


    public List<APIActionDescription> getAPIDescription() {
        if (actionList == null){
            actionList = loadAPIDescriptionFromFile(FILE_NAME);//new ArrayList<>();
//            actionList.add(new APIActionDescription(actionAPIPath, actionAPIMethod, actionAPIDescription));
//            actionList.add(new APIActionDescription(actionAgentPath, actionAgentMethod, actionAgentDescription));
//            actionList.add(new APIActionDescription(actionAgentsPath, actionAgentsMethod, actionAgentsDescription));
//            actionList.add(new APIActionDescription(actionAgentsAllPath, actionAgentsAllMethod, actionAgentsAllDescription));
        }
        return actionList;
    }


    private List<APIActionDescription> loadAPIDescriptionFromFile(String fileName){
        List<APIActionDescription> result = new ArrayList<>();
        BufferedReader bufferedReader;
        String line;
        StringTokenizer stringTokenizer;
        String path, method, description, parameters, res;
        int minNumberOfParameters = 3;


        try {
            bufferedReader = new BufferedReader(new FileReader(fileName));
            line = bufferedReader.readLine();
            while (line != null) {
                stringTokenizer = new StringTokenizer(line, ";");
                path = stringTokenizer.nextToken();
                method = stringTokenizer.nextToken();
                description = stringTokenizer.nextToken();
                parameters = stringTokenizer.nextToken();
                res = stringTokenizer.nextToken();

                result.add(new APIActionDescription(path, method, description, parameters, res));

                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }

        return result;
    }

}
