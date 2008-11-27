package com.googlecode.tlb.support.cruise;

import com.googlecode.tlb.domain.CurrentJob;
import com.googlecode.tlb.domain.GroupLoader;
import com.googlecode.tlb.domain.GroupsDivider;
import com.googlecode.tlb.support.twist.Group;
import com.googlecode.tlb.exceptions.JobNotFoundException;
import com.googlecode.tlb.utils.StringUtil;

import java.util.List;

public class AgentBasedGroupLoader implements GroupLoader {
    private final CruiseConnector connector;
    private final CurrentJob job;

    public AgentBasedGroupLoader(CruiseConnector connector, CurrentJob job) {
        this.connector = connector;
        this.job = job;
    }

    public Group load() throws JobNotFoundException {
        String jobName = job.getJobName();
        if (StringUtil.isEmpty(jobName)) {
            throw new JobNotFoundException(jobName);
        }
        String pipelineName = job.getPipelineName();
        String stageName = job.getStageName();
        String pipelinesJson = this.connector.pipelineStatus(pipelineName, stageName, jobName);
        JsonClient jsonClient = new JsonClient(pipelinesJson, pipelineName, stageName);
        List<String> allJobs = jsonClient.getJobsInStage();
        Group group = GroupsDivider.divid(allJobs, jobName);
        if (!isGroupFound(group)) {
            throw new RuntimeException("Running jobName " + jobName
                    + "cannot be found in Cruise stage definition.");
        }
        return group;
    }

    private boolean isGroupFound(Group group) {
        return group.jobsCount() > 0;
    }


}
