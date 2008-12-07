package com.googlecode.tlb;

import org.junit.After;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.Assert.assertThat;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import com.googlecode.tlb.utils.FileUtil;

public class SupportForCruiseWithSecurityTest {
    protected ServerIsRunning serverIsRunning;
    protected AgentIsRunning agentIsRunning;
    protected String pipeline;


    @After
    public void tearDown() throws Exception {
        serverIsRunning.stop();
        agentIsRunning.stop();
    }

    protected String getCruiseProperties(String job) {
        String url = "http://localhost:8153/cruise/properties/" + pipeline + "/history/defaultStage/" + job + ".json?limitLabel=1";
        HttpClient httpClient = new HttpClient();
        GetMethod method = new GetMethod(url);
        try {
            httpClient.executeMethod(method);
            return method.getResponseBodyAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            method.releaseConnection();
        }
    }

    protected void assertJobTestCount(String job, int count) {
        String cruiseProperties = getCruiseProperties(job);
        assertThat(cruiseProperties,
                cruiseProperties, containsString("\"tests_total_count\" : \"" + count + "\""));
    }

    protected static File createHgRepo(String pipelineName, String sourceProject) throws IOException, InterruptedException {
        File tempRepo = new File("target/" + pipelineName);
        org.apache.commons.io.FileUtils.deleteDirectory(tempRepo);
        org.apache.commons.io.FileUtils.copyDirectory(new File(sourceProject), tempRepo);
        FileUtils.copyFile(new File("target/test-load-balancer.jar"), new File(tempRepo, "lib/test-load-balancer.jar"));
        FileUtil.copyFile(new File("lib/production"), new File(tempRepo, "lib"));


        com.googlecode.tlb.utils.ExceptionUtils.bombIf(com.googlecode.tlb.utils.SystemUtil.runCommand(tempRepo, "hg", "init") != 0, "hg init failed!");
        com.googlecode.tlb.utils.ExceptionUtils.bombIf(com.googlecode.tlb.utils.SystemUtil.runCommand(tempRepo, "hg", "addremove") != 0, "hg addremove failed!");
        com.googlecode.tlb.utils.ExceptionUtils.bombIf(com.googlecode.tlb.utils.SystemUtil.runCommand(tempRepo, "hg", "ci", "-m", "Initial checkin") != 0, "hg ci failed");
        return tempRepo;
    }
}
