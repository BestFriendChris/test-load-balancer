package com.googlecode.tlb.support.cruise;

import com.googlecode.tlb.support.twist.Group;

import java.util.List;

public class GroupLoader {
    private CruiseConnector connector;

    public GroupLoader(CruiseConnector connector) {
        this.connector = connector;
    }


    public Group load(String pipelineName, String stageName, String jobName) {
        String jsonString = this.connector.pipelineStatus(pipelineName, stageName, jobName);
        JSONClient jsonClient = new JSONClient(jsonString, pipelineName, stageName);
        List<String> allJobs = jsonClient.getJobsInStage();
        return GroupsDivider.divid(allJobs, jobName);
    }
}
