package com.googlecode.tlb.support.cruise;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

public class CruiseUserSession implements CruiseConnector {
    private String username;
    private String password;

    public CruiseUserSession(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String pipelineStatus(String pipelineName, String stageName, String jobName) {
        String serverUrl = getPipelineStatusUrl();
        GetMethod method = new GetMethod(serverUrl);
        try {
            httpClient().executeMethod(method);
            return method.getResponseBodyAsString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    private HttpClient httpClient() {
        HttpClient httpClient = new HttpClient();
        if (needsAuthentication(username, password)) {
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            httpClient.getParams().setAuthenticationPreemptive(true);
            httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        }
        return httpClient;
    }

    private String getPipelineStatusUrl() {
        String serverUrl = System.getProperty("cruise.server.url");
        return serverUrl + "pipelineStatus.json";
    }

    private boolean needsAuthentication(String username, String password) {
        return username != null && password != null;
    }

}
