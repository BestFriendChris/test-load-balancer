package com.googlecode.tlb.support.cruise;

public interface CruiseConnector {
    String pipelineStatus(String pipelineName, String stageName, String jobName);
}
