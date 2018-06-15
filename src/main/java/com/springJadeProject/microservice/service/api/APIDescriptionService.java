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
    private List<APIActionDescription> actionList;

    public APIDescriptionService() {
    }

    public List<APIActionDescription> getActionList() {
        return actionList;
    }


    public List<APIActionDescription> getAPIDescription() {
        if (actionList == null){
            actionList = loadAPIDescriptionFromFile(FILE_NAME);
        }
        return actionList;
    }


    private List<APIActionDescription> loadAPIDescriptionFromFile(String fileName){
        List<APIActionDescription> result = new ArrayList<>();
        BufferedReader bufferedReader;
        String line;
        StringTokenizer stringTokenizer;
        String path, method, description, parameters, res;


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
