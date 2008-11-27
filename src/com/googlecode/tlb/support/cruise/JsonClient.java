package com.googlecode.tlb.support.cruise;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;

import java.util.List;
import java.util.ArrayList;
import java.io.StringReader;

public class JsonClient {
    private String pipelinesJson;
    private String pipelineName;
    private String stageName;
    public JSONArray pipelineArray;

    public JsonClient(String pipelinesJson, String pipelineName, String stageName) {
        this.pipelinesJson = pipelinesJson;
        this.pipelineName = pipelineName;
        this.stageName = stageName;
        pipelineArray = parsePipelines(pipelinesJson);
    }

    public List<String> getJobsInStage() {
        JSONObject pipelineJson = getPipeline(pipelineName);
        JSONObject stageJSON = getStage(pipelineJson, stageName);
        return getJobs(stageJSON);
    }

    private List<String> getJobs(JSONObject stageJsonObj) {
        JSONArray builds = (JSONArray) stageJsonObj.get("builds");
        List<String> jobNames = new ArrayList<String>();
        for (int i = 0; i < builds.size(); i++) {
            JSONObject tempJson = (JSONObject) builds.get(i);
            JSONString name = (JSONString) tempJson.get("name");
            jobNames.add(name.getValue());
        }
        return jobNames;
    }

    private JSONObject getStage(JSONObject pipelineJson, String stageName) {
        JSONArray stages = (JSONArray) pipelineJson.get("stages");
        return getItemByAttribute(stages, "stageName", stageName);
    }

    private JSONObject getPipeline(String pipelineName) {
        return getItemByAttribute(pipelineArray, "name", pipelineName);
    }

    private static JSONObject getItemByAttribute(JSONArray jsonArray, String attributeName, String attributeValue) {
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject child = (JSONObject) jsonArray.get(i);
            JSONString jsonString = (JSONString) child.get(attributeName);
            if (attributeValue.equals(jsonString.getValue())) {
                return child;
            }
        }
        return null;
    }

    private static JSONArray parsePipelines(String pipelinesJson) {
        JSONValue jsonValue = parseJsonValue(pipelinesJson);
        if (jsonValue instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) jsonValue;
            JSONValue pipelines = jsonObject.get("pipelines");
            if (pipelines instanceof JSONArray) {
                return (JSONArray) pipelines;
            }
        }
        throw new IllegalArgumentException("Illegal pipelines json: " + pipelinesJson);
    }

    private static JSONValue parseJsonValue(String json) {
        JSONParser parser = new JSONParser(new StringReader(json));
        JSONValue jsonValue;
        try {
            jsonValue = parser.nextValue();
        } catch (Exception e) {
            throw new RuntimeException("Illegal json: " + json, e);
        }
        return jsonValue;
    }


}
