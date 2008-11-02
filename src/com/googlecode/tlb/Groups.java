package com.googlecode.tlb;

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
        return null;
    }
}
