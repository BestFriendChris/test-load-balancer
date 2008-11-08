package com.googlecode.tlb.support.cruise;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class JSONClient {
    private String jsonString;
    private String pipelineName;
    private String stageName;

    public JSONClient(String jsonString, String pipelineName, String stageName) {
        this.jsonString = jsonString;
        this.pipelineName = pipelineName;
        this.stageName = stageName;
    }

    public List<String> getJobsInStage() {
        JSONObject pipelineJSON = getPipeline(jsonString, pipelineName);
        JSONObject stageJSON = getStage(pipelineJSON, stageName);
        return getJobs(stageJSON);
    }

    private List<String> getJobs(JSONObject stageJsonObj) {
        JSONArray stages = (JSONArray) stageJsonObj.get("builds");
        List<String> jobs = new ArrayList<String>();
        int anothersize = stages.size();
        for (int i = 0; i < anothersize; i++) {
            JSONObject tempJson = (JSONObject) stages.get(i);
            JSONString name = (JSONString) tempJson.get("name");
            jobs.add(name.getValue());
        }
        return jobs;
    }

    private JSONObject getStage(JSONObject json, String stageName) {
        JSONArray stages = (JSONArray) json.get("stages");
        int anothersize = stages.size();
        for (int i = 0; i < anothersize; i++) {
            JSONObject tempJson = (JSONObject) stages.get(i);
            JSONString name = (JSONString) tempJson.get("stageName");
            if (stageName.equals(name.getValue())) {
                return tempJson;
            }
        }
        return null;
    }


    private JSONObject getPipeline(String pipelinesJson, String pipelineName) {
        try {
            JSONObject jsonObject = (JSONObject) parseJsonObject(pipelinesJson);
            JSONArray pipelines = (JSONArray) jsonObject.get("pipelines");
            int size = pipelines.size();
            JSONObject pipelineJson = null;
            for (int i = 0; i < size; i++) {
                JSONObject tempJson = (JSONObject) pipelines.get(i);
                JSONString name = (JSONString) tempJson.get("name");
                if (pipelineName.equals(name.getValue())) {
                    pipelineJson = tempJson;
                }
            }
            return pipelineJson;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONValue parseJsonObject(String pipelineJson) {
        InputStream is = new ByteArrayInputStream(pipelineJson.getBytes());
        JSONParser parser = new JSONParser(is);
        JSONValue pipelineJsonObj = null;
        try {
            pipelineJsonObj = parser.nextValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pipelineJsonObj;
    }


}
