package com.googlecode.tlb.support.cruise;

import static com.googlecode.tlb.utils.ExceptionUtils.bomb;

import java.io.File;
import java.io.IOException;

public class CruiseAgentSession implements CruiseConnector {
    private AgentProxy agentProxy;
    public static final String SERVER_URL_KEY = "CRUISE_SERVER_URL";

    public CruiseAgentSession() {
        File agentConfigDir = new AgentConfigDirLocator().agentConfigDir();
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
        String serverUrl = System.getenv(SERVER_URL_KEY);
        if (!serverUrl.endsWith("/")) {
            serverUrl += "/";
        }
        return serverUrl + "remoting/admin/pipelineStatus.json";
    }
}
