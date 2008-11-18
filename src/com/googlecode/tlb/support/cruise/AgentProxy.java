package com.googlecode.tlb.support.cruise;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.io.File;

import com.googlecode.tlb.support.cruise.security.AuthSSLProtocolSocketFactory;

public class AgentProxy {
    private static final String AGENT_STORE_PASSWORD = "agent5s0repa55w0rd";
    private File agentCertificateFile;
    private File agentTrustFile;

    public AgentProxy(File agentConfigDir) {
        agentCertificateFile = new File(agentConfigDir, "agent.jks");
        agentTrustFile = new File(agentConfigDir, "trust.jks");
        createSslInfrastructure();
    }

    private void createSslInfrastructure() {
        AuthSSLProtocolSocketFactory protocolSocketFactory = new AuthSSLProtocolSocketFactory(
                agentTrustFile, agentCertificateFile, AGENT_STORE_PASSWORD);
        Protocol.registerProtocol("http", new Protocol("http", (ProtocolSocketFactory) protocolSocketFactory, 80));
        Protocol.registerProtocol("https", new Protocol("https", (ProtocolSocketFactory) protocolSocketFactory, 443));
    }

    public ResponseResult get(String url) throws IOException {
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        try {
            httpClient.executeMethod(getMethod);
            int statusCode = getMethod.getStatusCode();
            String responseBody = getMethod.getResponseBodyAsString();
            return new ResponseResult(statusCode, responseBody);
        } finally {
            getMethod.releaseConnection();
        }
    }

    public ResponseResult post(String url, NameValuePair... params) throws IOException {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        try {
            postMethod.setRequestBody(params);
            httpClient.executeMethod(postMethod);
            int statusCode = postMethod.getStatusCode();
            String responseBody = postMethod.getResponseBodyAsString();
            return new ResponseResult(statusCode, responseBody);
        } finally {
            postMethod.releaseConnection();
        }
    }
}
