package com.googlecode.tlb.support.junit;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.log4j.Logger;
import com.googlecode.tlb.domain.Group;
import com.googlecode.tlb.domain.GroupLoader;
import com.googlecode.tlb.exceptions.JobNotFoundException;
import com.googlecode.tlb.domain.GroupLoaderFactory;
import com.googlecode.tlb.domain.LoadBalancer;

import java.util.Iterator;

public class FilterFileSet extends FileSet {
    public static final Logger LOGGER = Logger.getLogger(FilterFileSet.class);
    private final GroupLoader groupLoader;
    private final LoadBalancer loadBalancer;

    public FilterFileSet(GroupLoader groupLoader, LoadBalancer loadBalancer) {
        this.groupLoader = groupLoader;
        this.loadBalancer = loadBalancer;
    }

    // Used in Ant
    public FilterFileSet() {
        this(GroupLoaderFactory.getInstance(), new JUnitLoadBalancer());
    }

    public Iterator iterator() {
        Iterator resources = super.iterator();
        try {
            final Group group = groupLoader.load();
            return loadBalancer.balance(resources, group.jobsCount(), group.jobIndex());
        } catch (JobNotFoundException e) {
            LOGGER.error("Failed to load balance", e);
            System.err.println("Failed to load balance: " + e);
            return resources;
        } catch (Exception e) {
            // TODO - fix log4j
            LOGGER.error("Failed to load balance", e);
            System.err.println("Failed to load balance: " + e);
            throw new BuildException(e);
        }
    }

}
