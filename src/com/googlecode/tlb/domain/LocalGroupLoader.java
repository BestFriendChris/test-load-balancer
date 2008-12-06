package com.googlecode.tlb.domain;

import com.googlecode.tlb.support.twist.Group;
import com.googlecode.tlb.exceptions.JobNotFoundException;

public class LocalGroupLoader implements GroupLoader {
    public Group load() throws JobNotFoundException {
        return new Group() {
            @Override
            public int jobsCount() {
                return 1;
            }
        };
    }
}
