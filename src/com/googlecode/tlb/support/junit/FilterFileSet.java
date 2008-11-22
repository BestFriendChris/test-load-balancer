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
import com.googlecode.tlb.support.cruise.GroupLoader;
import com.googlecode.tlb.support.cruise.CruiseAgentSession;
import com.googlecode.tlb.support.cruise.CruiseConnector;
import com.googlecode.tlb.exceptions.JobNotFoundException;
import com.googlecode.tlb.domain.CurrentJob;
import com.googlecode.tlb.domain.GroupLoaderFactory;

import java.util.Iterator;
import java.io.ByteArrayInputStream;

public class FilterFileSet extends FileSet {
    public static final Logger LOGGER = Logger.getLogger(FilterFileSet.class);
    private GroupLoader groupLoader;
    private CurrentJob currentJob;

    public FilterFileSet(GroupLoader groupLoader, CurrentJob currentJob) {
        this.groupLoader = groupLoader;
        this.currentJob = currentJob;
    }

    public FilterFileSet() {
        this.groupLoader = GroupLoaderFactory.getInstance();
        this.currentJob = new CurrentJob();
    }

    public Iterator iterator() {
        try {
            final Group group = groupLoader.load();
            return new JUnitLoadBalancer().balance(super.iterator(), group.jobsCount(),
                    group.jobIndex(currentJob.getJobName()));
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
    }

}
