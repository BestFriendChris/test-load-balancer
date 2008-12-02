package com.googlecode.tlb.support.twist;

import com.thoughtworks.twist.core.execution.ant.ExecuteScenariosTask;

import java.io.File;

public class ExecuteScenariosTaskWrapper extends ExecuteScenariosTask {

    
    @Override
    public void setScenarioDir(File file) {
        super.setScenarioDir(file);
    }
}
