package com.googlecode.tlb.domain;

public class CurrentJob {
    public static final String JOB_NAME_PROP = "cruise.job.name";
    public static final String PIPELINE_NAME_PROP = "cruise.pipeline.name";
    public static final String STAGE_NAME_PROP = "cruise.stage.name";

    public String getJobName() {
        return System.getProperty(JOB_NAME_PROP);
    }

    public String getStageName() {
        return System.getProperty(STAGE_NAME_PROP);
    }

    public String getPipelineName() {
        return System.getProperty(PIPELINE_NAME_PROP);
    }
}
