package com.googlecode.tlb.support.cruise;

import com.googlecode.tlb.exceptions.JobNotFoundException;
import com.googlecode.tlb.support.twist.Group;
import com.googlecode.tlb.domain.GroupLoader;

import java.util.Map;

public class EnvBasedGroupLoader implements GroupLoader {
    public static final String PIECES = "PIECES";
    public static final String INDEX = "INDEX";

    public EnvBasedGroupLoader() {

    }

    public Group load() throws JobNotFoundException {
        return new Group() {
            public void addJob(String jobName) {
                throw new RuntimeException("Not implemented");
            }

            public String jobAt(int i) {
                throw new RuntimeException("Not implemented");
            }

            public String toString() {
                return super.toString();
            }

            public Boolean contains(String job) {
                throw new RuntimeException("Not implemented");
            }

            public int jobsCount() {
                return Integer.parseInt(System.getenv(PIECES));

            }

            public int jobIndex(String jobName) {
                return Integer.parseInt(System.getenv(INDEX));
            }
        };
    }

    public static boolean satisfy(Map<String, String> envs) {
        return envs.get(INDEX) != null && envs.get(PIECES) != null;
    }
}
