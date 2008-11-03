package com.googlecode.tlb.support.twist;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.log4j.Logger;

import java.io.File;

import com.googlecode.tlb.domain.LoadBalancer;

public class TwistLoadBalancer extends Task {
    public static final Logger LOGGER = Logger.getLogger(TwistLoadBalancer.class);
    private File scenarioDir;
    private String loadBalance;

    public void execute() throws BuildException {
        try {
            LoadBalancer.getLoadBalancer(loadBalance).balance(scenarioDir);
        } catch (Exception e) {
            // TODO - fix log4j
            LOGGER.error("Failed to load balance", e);
            System.err.println("Failed to load balance: " + e);
            throw new BuildException(e);
        }
    }

    public void setScenarioDir(File scenarioDir) {
        this.scenarioDir = scenarioDir;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }
}
