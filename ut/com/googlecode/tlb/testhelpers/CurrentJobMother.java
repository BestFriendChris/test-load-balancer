package com.googlecode.tlb.testhelpers;

import com.googlecode.tlb.domain.CurrentJob;

public class CurrentJobMother {
    public static CurrentJob currentJobStub(final String jobName, final String stageName, final String pipelineName) {
        return new CurrentJob(){
            public String getJobName() {
                return jobName;
            }

            public String getStageName() {
                return stageName;
            }

            public String getPipelineName() {
                return pipelineName;
            }
        };
    }
}
