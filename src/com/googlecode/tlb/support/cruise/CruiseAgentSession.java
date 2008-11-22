package com.googlecode.tlb.support.cruise;

import static com.googlecode.tlb.utils.ExceptionUtils.bomb;

import java.io.File;
import java.io.IOException;

public class CruiseAgentSession implements CruiseConnector {
    private AgentProxy agentProxy;

    public CruiseAgentSession() {
        File agentConfigDir = new File("../config");
        this.agentProxy = new AgentProxy(agentConfigDir);
    }

    public String pipelineStatus(String pipelineName, String stageName, String jobName) {
        try {
            return agentProxy.get(getPipelineStatusUrl()).getResponseBody();
        } catch (IOException e) {
            throw bomb(e);
        }
    }

    private String getPipelineStatusUrl() {
        String serverUrl = System.getProperty("cruise.server.url");
        return serverUrl + "pipelineStatus.json";
    }
}
