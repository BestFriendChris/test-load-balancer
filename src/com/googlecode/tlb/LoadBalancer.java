package com.googlecode.tlb;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;

import java.io.ByteArrayInputStream;
import java.io.File;

import com.googlecode.tlb.parser.GroupLexer;
import com.googlecode.tlb.parser.GroupParser;

/**
 *
 */
public class LoadBalancer {
    private String definition;
    private String jobName;
    private final Group myGroup;
    public static final String CRUISE_JOB_NAME = "cruise.job.name";
    private static final String JOBNAME = "CRUISE_JOB_NAME";


    private LoadBalancer(Group myGroup, String jobName) {
        this.myGroup = myGroup;
        this.jobName = jobName;
    }

    public static LoadBalancer getLoadBalancer(String definition) {
        String jobName = getJobName(System.getenv(JOBNAME));
        Groups groups = null;
        try {
            ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(definition.getBytes("UTF-8")));
            GroupLexer lexer = new GroupLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            GroupParser parser = new GroupParser(tokens);
            groups = parser.groups();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final Group group = groups.findByJobName(jobName);
        return new LoadBalancer(group, jobName);
    }

    static String getJobName(String envValue) {
        String jobName = System.getProperty(CRUISE_JOB_NAME);
        if (isEmpty(jobName)) {
            jobName = envValue;
        }
        return jobName;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public void balance(File scenariosDir) {
        if (isEmpty(jobName)) {
            throw new RuntimeException("Unable to find the current running job. Cruise should set it automatically.");
        }
        final LoadBalanceFactor factor = new LoadBalanceFactor(myGroup.jobIndex(jobName), myGroup.jobsCount());
        new TwistScenarioFilter(scenariosDir).filter(factor);
    }
}
