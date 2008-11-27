package com.googlecode.tlb.support.junit;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.log4j.Logger;
import com.googlecode.tlb.support.twist.Group;
import com.googlecode.tlb.domain.GroupLoader;
import com.googlecode.tlb.exceptions.JobNotFoundException;
import com.googlecode.tlb.domain.GroupLoaderFactory;

import java.util.Iterator;

public class FilterFileSet extends FileSet {
    public static final Logger LOGGER = Logger.getLogger(FilterFileSet.class);
    private GroupLoader groupLoader;

    public FilterFileSet(GroupLoader groupLoader) {
        this.groupLoader = groupLoader;
    }

    public FilterFileSet() {
        this.groupLoader = GroupLoaderFactory.getInstance();
    }

    public Iterator iterator() {
        try {
            final Group group = groupLoader.load();
            return new JUnitLoadBalancer().balance(super.iterator(), group.jobsCount(), group.jobIndex());
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
