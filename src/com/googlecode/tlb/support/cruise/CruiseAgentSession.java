package com.googlecode.tlb.support.cruise;

import static com.googlecode.tlb.utils.ExceptionUtils.bomb;

import java.io.File;
import java.io.IOException;
import static java.lang.String.format;

public class CruiseAgentSession implements CruiseConnector {
    private AgentProxy agentProxy;
    public static final String SERVER_URL_KEY = "CRUISE_SERVER_URL";

    public CruiseAgentSession() {
        File agentConfigDir = new AgentConfigDirLocator().agentConfigDir();
        this.agentProxy = new AgentProxy(agentConfigDir);
    }

    public String pipelineStatus(String pipelineName, String stageName, String jobName) {
        try {
            String url = pipelineStatusUrl();
            System.out.println(format("Getting pipeline status from [%s] for pipeline [%s] stage [%s] job [%s]",
                    url, pipelineName, stageName, jobName));
            return agentProxy.getResourceAsString(url);
        } catch (IOException e) {
            throw bomb(e);
        }
    }

    public String consoleOut(String pipelineName, String stageName, String jobName) {
        String json = pipelineStatus(pipelineName, stageName, jobName);
        JsonClient client = new JsonClient(json, pipelineName, stageName);
        String jobId = client.getJobId(jobName);
        String url = consoleOutUrl(jobId);
        try {
            return agentProxy.getResourceAsString(url);
        } catch (IOException e) {
            throw bomb(e);
        }
    }

    private String consoleOutUrl(String jobId) {
        return urlPrefix() + "repository/artifacts/consoleout?buildInstanceId=" + jobId;
    }

    private String pipelineStatusUrl() {
        return urlPrefix() + "admin/pipelineStatus.json";
    }

    private String urlPrefix() {
        return contextRoot() + "remoting/";
    }

    private String contextRoot() {
        String serverUrl = System.getenv(SERVER_URL_KEY);
        serverUrl = serverUrl == null ? "https://localhost:8154/cruise/" : serverUrl;
        if (!serverUrl.endsWith("/")) {
            serverUrl += "/";
        }
        return serverUrl;
    }
}
