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
import com.googlecode.tlb.exceptions.JobNotFoundException;

import java.util.Iterator;
import java.io.ByteArrayInputStream;

public class FilterFileSet extends FileSet {
    public static final Logger LOGGER = Logger.getLogger(FilterFileSet.class);

    private static final String JOB_NAME_ENV = "CRUISE_JOB_NAME";
    public static final String JOB_NAME_PROP = "cruise.job.name";

    String loadBalance;

    public Iterator iterator() {
        try {
            String jobName = getJobName(System.getenv(JOB_NAME_ENV));
            Groups groups;
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
            return new JUnitLoadBalancer().balance(super.iterator(), group.jobsCount(), group.jobIndex(jobName));
        } catch (JobNotFoundException e) {
            LOGGER.error("Failed to load balance", e);
            System.err.println("Failed to load balance: " + e);
            return super.iterator();
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

    static String getJobName(String envValue) throws JobNotFoundException {
        String jobName = System.getProperty(JOB_NAME_PROP);
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

}
