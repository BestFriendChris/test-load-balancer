package com.googlecode.tlb;

import java.util.ArrayList;
import java.util.List;

public class Group {
    public List<String> jobs = new ArrayList<String>();

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
}
