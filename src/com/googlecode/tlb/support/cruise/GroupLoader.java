package com.googlecode.tlb.support.cruise;

import com.googlecode.tlb.support.twist.Group;
import com.googlecode.tlb.utils.StringUtil;
import com.googlecode.tlb.exceptions.JobNotFoundException;

import java.util.List;

public class GroupLoader {
    private CruiseConnector connector;

    public GroupLoader(CruiseConnector connector) {
        this.connector = connector;
    }


    public Group load(String pipelineName, String stageName, String jobName) throws JobNotFoundException {
        if (StringUtil.isEmpty(jobName)) {
            throw new JobNotFoundException(jobName);
        }
        String jsonString = this.connector.pipelineStatus(pipelineName, stageName, jobName);
        JSONClient jsonClient = new JSONClient(jsonString, pipelineName, stageName);
        List<String> allJobs = jsonClient.getJobsInStage();
        Group group = GroupsDivider.divid(allJobs, jobName);
        if (!isGroupFound(group)) {
            throw new RuntimeException("Running jobName " + jobName + "cannot be found in Cruise stage definition.");
        }
        return group;
    }

    private boolean isGroupFound(Group group) {
        return group.jobsCount() > 0;
    }


}
