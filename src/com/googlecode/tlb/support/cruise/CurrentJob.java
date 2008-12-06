package com.googlecode.tlb.support.cruise;

public class CurrentJob {
    public static final String PIPELINE_NAME_KEY = "CRUISE_PIPELINE_NAME";
    public static final String STAGE_NAME_KEY = "CRUISE_STAGE_NAME";
    public static final String JOB_NAME_KEY = "CRUISE_JOB_NAME";
    public static final String CRUISE_SERVER_URL = "CRUISE_SERVER_URL";

    public String getJobName() {
        return System.getenv(JOB_NAME_KEY);
    }

    public String getStageName() {
        return System.getenv(STAGE_NAME_KEY);
    }

    public String getPipelineName() {
        return System.getenv(PIPELINE_NAME_KEY);
    }
}
