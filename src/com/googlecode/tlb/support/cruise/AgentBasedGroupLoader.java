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

        if (StringUtil.isEmpty(job.getJobName())) {
            throw new JobNotFoundException(job.getJobName());
        }
        String jsonString = this.connector.pipelineStatus(job.getPipelineName(), job.getStageName(), job.getJobName());
        JSONClient jsonClient = new JSONClient(jsonString, job.getPipelineName(), job.getStageName());
        List<String> allJobs = jsonClient.getJobsInStage();
        Group group = GroupsDivider.divid(allJobs, job.getJobName());
        if (!isGroupFound(group)) {
            throw new RuntimeException("Running jobName " + job.getJobName()
                    + "cannot be found in Cruise stage definition.");
        }
        return group;
    }

    private boolean isGroupFound(Group group) {
        return group.jobsCount() > 0;
    }


}
