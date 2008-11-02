package com.googlecode.tlb;

/**
 *
 */
public class LoadBalancer {
    private final String definition;
    private String jobName;


    public LoadBalancer(String definition) {
        this.definition = definition;
        //Look at the BuildWork, the job name has been put into the system property
        jobName = System.getProperty("cruise.job.name");
    }

    LoadBalancer(String definition, String jobName) {
        this.definition = definition;
        this.jobName = jobName;
    }


    public LoadBalanceFactor balance() {
        return null;
    }
}
