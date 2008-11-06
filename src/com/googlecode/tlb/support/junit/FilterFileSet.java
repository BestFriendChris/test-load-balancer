package com.googlecode.tlb.support.junit;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRInputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.log4j.Logger;
import com.googlecode.tlb.support.twist.Group;
import com.googlecode.tlb.support.twist.Groups;
import com.googlecode.tlb.support.twist.parser.GroupLexer;
import com.googlecode.tlb.support.twist.parser.GroupParser;

import java.util.Iterator;
import java.io.ByteArrayInputStream;

public class FilterFileSet extends FileSet {
    private static final String JOBNAME = "CRUISE_JOB_NAME";
    public static final Logger LOGGER = Logger.getLogger(FilterFileSet.class);
    String loadBalance;


    public static final String CRUISE_JOB_NAME = "cruise.job.name";

    public Iterator iterator() {
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
            return new JUnitLoadBalancer(group.jobIndex(jobName), group.jobsCount()).balance(super.iterator());
        } catch (Exception e) {
            // TODO - fix log4j
            LOGGER.error("Failed to load balance", e);
            System.err.println("Failed to load balance: " + e);
            throw new BuildException(e);
        }
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    static String getJobName(String envValue) {
        String jobName = System.getProperty(CRUISE_JOB_NAME);
        if (isEmpty(jobName)) {
            jobName = envValue;
        }
        if (isEmpty(jobName)) {
            throw new RuntimeException("Unable to find the current running job. Cruise should set it automatically.");
        }
        return jobName;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

}
