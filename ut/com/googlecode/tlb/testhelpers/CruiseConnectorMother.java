package com.googlecode.tlb.testhelpers;

import com.googlecode.tlb.support.cruise.CruiseConnector;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class CruiseConnectorMother {
    public static CruiseConnector connectorStub() {
        return new CruiseConnector() {
            public String pipelineStatus(String pipelineName, String stageName, String jobName) {
                File file = new File("ut/com/googlecode/tlb/support/cruise/pipelineStatusJson.txt");
                try {
                    return FileUtils.readFileToString(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        };
    }
}
