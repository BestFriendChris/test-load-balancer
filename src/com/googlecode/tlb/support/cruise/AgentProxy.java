package com.googlecode.tlb.support.cruise;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import static org.apache.commons.httpclient.protocol.Protocol.registerProtocol;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.io.File;
import static java.lang.String.format;

import com.googlecode.tlb.support.cruise.security.AuthSSLProtocolSocketFactory;
import static com.googlecode.tlb.utils.ExceptionUtils.bombIf;
import static com.googlecode.tlb.utils.ExceptionUtils.bombUnless;

public class AgentProxy {
    private static final String AGENT_STORE_PASSWORD = "agent5s0repa55w0rd";
    private File agentCertificateFile;
    private File agentTrustFile;
    public static final String AGENT_JKS = "agent.jks";
    public static final String TRUST_JKS = "trust.jks";
    private final Protocol httpProtocol;
    private final Protocol httpsProtocol;

    public AgentProxy(File agentConfigDir) {
        agentCertificateFile = new File(agentConfigDir, AGENT_JKS);
        bombUnless(agentCertificateFile.exists(),
                format("File %s not exist!", agentCertificateFile.getAbsolutePath()));
        agentTrustFile = new File(agentConfigDir, TRUST_JKS);
        bombUnless(agentTrustFile.exists(),
                format("File %s not exist!", agentTrustFile.getAbsolutePath()));
        System.out.println("Using agent certificate file: " + agentCertificateFile.getAbsolutePath());
        System.out.println("Using agent trust file: " + agentTrustFile.getAbsolutePath());

        AuthSSLProtocolSocketFactory protocolSocketFactory = new AuthSSLProtocolSocketFactory(
                agentTrustFile, agentCertificateFile, AGENT_STORE_PASSWORD);
        httpProtocol = new Protocol("http", (ProtocolSocketFactory) protocolSocketFactory, 80);
        httpsProtocol = new Protocol("https", (ProtocolSocketFactory) protocolSocketFactory, 443);
    }

    private void registerProtocol() {
        Protocol.registerProtocol("http", httpProtocol);
        Protocol.registerProtocol("https", httpsProtocol);
    }

    private void unregisterProtocol() {
        Protocol.unregisterProtocol("http");
        Protocol.unregisterProtocol("https");
    }

    public ResponseResult get(String url) throws IOException {
        registerProtocol();
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        try {
            httpClient.executeMethod(getMethod);
            int statusCode = getMethod.getStatusCode();
            String responseBody = getMethod.getResponseBodyAsString();
            return new ResponseResult(statusCode, responseBody);
        } finally {
            getMethod.releaseConnection();
            unregisterProtocol();
        }
    }

    public ResponseResult post(String url, NameValuePair... params) throws IOException {
        registerProtocol();
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
            unregisterProtocol();
        }
    }

    public String getResourceAsString(String url) throws IOException {
        return get(url).getResponseBody();
    }
}
