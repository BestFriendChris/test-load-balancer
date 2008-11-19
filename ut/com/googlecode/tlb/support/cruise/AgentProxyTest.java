package com.googlecode.tlb.support.cruise;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.Assert.assertThat;
import org.apache.commons.httpclient.NameValuePair;
import org.hamcrest.core.Is;
import static org.hamcrest.core.Is.is;

import java.io.File;
import java.io.IOException;

public class AgentProxyTest {
    private AgentProxy agentProxy;
    private static final String BASE_URL = "https://localhost:8154/cruise";

    @Before
    public void setUp() throws Exception {
        //String configDir = "/home/leon/hg-cruise/cruise/target/cruise-agent-1.0/config";
        //agentProxy = new AgentProxy(new File(configDir));
    }

    @Test
    @Ignore
    public void shouldCanAccessBuildProperties() throws IOException {
        String url = BASE_URL + "/remoting/properties/testproj/1/defaultStage/defaultJob/key1";
        ResponseResult response = agentProxy.post(url, new NameValuePair("value", "value1"));
        assertThat(response.getReturnCode(), is(200));
    }

    @Test
    @Ignore
    public void shouldCanAccessPipelineStatusJson() throws IOException {
        String url = BASE_URL + "/remoting/admin/pipelineStatus.json";
        ResponseResult response = agentProxy.get(url);
        assertThat(response.getReturnCode(), is(200));
        String pipelineStatusJson = response.getResponseBody();
        System.out.println(pipelineStatusJson);
        assertThat(pipelineStatusJson, containsString("testproj"));
    }
}
