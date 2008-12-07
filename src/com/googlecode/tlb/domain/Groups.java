package com.googlecode.tlb.domain;

import java.util.ArrayList;

public class Groups extends ArrayList<Group> {
    public Groups() {

    }

    public Groups(Group... groups) {
        for (Group group : groups) {
            this.add(group);
        }
    }

    public Group findByJobName(String jobName) {
        for (Group group : this) {
            if (group.contains(jobName)) {
                return group;
            }
        }
        throw new RuntimeException(String.format("Failed to find Job [%s] in any Group of Groups %s. " +
                "Please make sure this job exists in your cruise config file", jobName, this));
    }
}
