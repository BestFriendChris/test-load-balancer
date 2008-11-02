package com.googlecode.tlb;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.File;

public class TwistLoadBalancer extends Task {
    private File scenarioDir;
    private String loadBalance;


    public void execute() throws BuildException {

        //input loadBalanerStragegy, output 1/3
        // 1. parse load balance => factor
        // 2. parse pipelineStatus => #

        //ScenariosModifier input 1/3, void, repose: changed the scenarios.
        // 3. parse scenariodir => #/factor for current agent
        // 4. ignore the rest
    }

    public void setScenarioDir(File scenarioDir) {
        this.scenarioDir = scenarioDir;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }
}
