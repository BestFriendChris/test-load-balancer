package com.googlecode.tlb.support.twist;

import java.util.ArrayList;
import java.util.List;
import static java.lang.String.format;

public class Group {
    public List<String> jobs = new ArrayList<String>();
    private String thisJob;

    public Group() {
    }

    public Group(String... jobs) {
        for (String job : jobs) {
            this.addJob(job);
        }
    }

    public Group(String[] jobArray, String thisJob) {
        this(jobArray);
        this.thisJob = thisJob;
    }

    public void addJob(String jobName) {
        this.jobs.add(jobName);
    }

    public String jobAt(int i) {
        return jobs.get(i);
    }

    public String toString() {
        return jobs.toString();
    }

    public Boolean contains(String job) {
        return jobs.contains(job);
    }

    public int jobsCount() {
        return jobs.size();
    }

    public int jobIndex(String jobName) {
        if (!jobs.contains(jobName)) {
            throw new RuntimeException(format("Job [%s] is not found", jobName));
        }
        return jobs.indexOf(jobName) + 1;
    }

    public int jobIndex() {
        return jobIndex(thisJob);
    }
}
