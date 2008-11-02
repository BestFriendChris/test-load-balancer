package com.googlecode.tlb;

import java.util.List;
import java.util.ArrayList;

/**
 *
 */
public class Group {
    public List jobs = new ArrayList();

    public void addJob(String jobName) {
        this.jobs.add(jobName);
    }
}
