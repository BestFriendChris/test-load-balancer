package com.googlecode.tlb;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;

public class TwistLoadBalancer extends Task {
    private File scenarioDir;
    private String loadBalance;


    public void execute() throws BuildException {
        LoadBalancer.getLoadBalancer(loadBalance).balance(scenarioDir);
    }

    public void setScenarioDir(File scenarioDir) {
        this.scenarioDir = scenarioDir;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }
}
