package com.googlecode.tlb;

import com.thoughtworks.twist.core.execution.ant.ExecuteScenariosTask;

public class TestLoadBalancerTask extends ExecuteScenariosTask {


    public void execute() {
        String tags = splitTagsForThisAgent();
        this.setTags(tags);

        super.execute();
    }

    private String splitTagsForThisAgent() {
        String[] existingTags = {"1", "2" };
        return "1";
    }

}
