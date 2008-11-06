package com.googlecode.tlb.support.twist;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.log4j.Logger;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;

import java.io.File;
import java.io.ByteArrayInputStream;

import com.googlecode.tlb.domain.LoadBalancer;
import com.googlecode.tlb.support.twist.parser.GroupLexer;
import com.googlecode.tlb.support.twist.parser.GroupParser;
import com.googlecode.tlb.exceptions.JobNotFoundException;

public class TwistLoadBalancer extends Task {
    public static final Logger LOGGER = Logger.getLogger(TwistLoadBalancer.class);
    private File scenarioDir;
    private String loadBalance;
    public static final String JOBNAME = "CRUISE_JOB_NAME";
    public static final String CRUISE_JOB_NAME = "cruise.job.name";

    public void execute() throws BuildException {
        try {
            String jobName = getJobName(System.getenv(JOBNAME));
            Groups groups = null;
            try {
                ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(loadBalance.getBytes("UTF-8")));
                GroupLexer lexer = new GroupLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                GroupParser parser = new GroupParser(tokens);
                groups = parser.groups();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            final Group group = groups.findByJobName(jobName);
            LoadBalancer.getLoadBalancer(group.jobIndex(jobName), group.jobsCount()).balance(scenarioDir);
        } catch (JobNotFoundException e) {
            LOGGER.error("Failed to load balance", e);
            System.err.println("Failed to load balance: " + e);
        } catch (Exception e) {
            // TODO - fix log4j
            LOGGER.error("Failed to load balance", e);
            System.err.println("Failed to load balance: " + e);
            throw new BuildException(e);
        }
    }

    static String getJobName(String envValue) throws JobNotFoundException {
        String jobName = System.getProperty(CRUISE_JOB_NAME);
        if (isEmpty(jobName)) {
            jobName = envValue;
        }
        if (isEmpty(jobName)) {
            throw new JobNotFoundException("Unable to find the current running job. Cruise should set it automatically.");
        }
        return jobName;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public void setScenarioDir(File scenarioDir) {
        this.scenarioDir = scenarioDir;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }
}
