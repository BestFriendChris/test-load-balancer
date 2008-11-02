package com.googlecode.tlb;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.io.ByteArrayInputStream;
import java.io.File;

import com.googlecode.tlb.parser.GroupLexer;
import com.googlecode.tlb.parser.GroupParser;

/**
 *
 */
public class LoadBalancer {
    private String definition;
    private String jobName;
    private final Group myGroup;


    private LoadBalancer(Group myGroup, String jobName) {
        this.myGroup = myGroup;
        this.jobName = jobName;
    }

    public static LoadBalancer getLoadBalancer(String definition) {
        String jobName = System.getProperty("cruise.job.name");
        Groups groups = null;
        try {
            ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(definition.getBytes("UTF-8")));
            GroupLexer lexer = new GroupLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            GroupParser parser = new GroupParser(tokens);
            groups = parser.groups();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final Group group = groups.findGroup(jobName);
        return new LoadBalancer(group, jobName);
    }

    public void balance(File scenariosDir) {
        final LoadBalanceFactor factor = new LoadBalanceFactor(myGroup.jobIndex(jobName), myGroup.jobsCount());
        new TwistScenarioFilter(scenariosDir).filter(factor);
    }
}
