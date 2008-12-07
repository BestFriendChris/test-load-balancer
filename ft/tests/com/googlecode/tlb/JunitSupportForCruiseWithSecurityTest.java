package com.googlecode.tlb;

import static com.googlecode.tlb.utils.ExceptionUtils.bomb;
import static com.googlecode.tlb.utils.ExceptionUtils.bombIf;
import com.googlecode.tlb.utils.FileUtil;
import static com.googlecode.tlb.utils.SystemUtil.runCommand;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.copyDirectory;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class JunitSupportForCruiseWithSecurityTest {
    private ServerIsRunning serverIsRunning;
    private AgentIsRunning agentIsRunning;
    private String pipeline;

    @Before
    public void setUp() throws Exception {
        File hgRepo = createHgRepo("connectfour");
        pipeline = UUID.randomUUID().toString();
        serverIsRunning = new ServerIsRunning(hgRepo.getAbsolutePath(), pipeline);
        agentIsRunning = new AgentIsRunning();
        serverIsRunning.start();
        agentIsRunning.start();
    }

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
        System.out.println(cruiseProperties);
        assertThat(cruiseProperties, containsString("\"tests_total_count\" : \"" + count + "\""));
    }

    @Test
    public void shouldRunUnderCruise() throws Exception {
        Thread.sleep(90000);
        assertJobTestCount("job-1", 11);
        assertJobTestCount("job-2", 10);
    }


    private static File createHgRepo(String pipelineName) throws IOException, InterruptedException {
        File tempRepo = new File("target/" + pipelineName);
        deleteDirectory(tempRepo);
        copyDirectory(new File("ft/junit/connectfour"), tempRepo);
        FileUtils.copyFile(new File("target/test-load-balancer.jar"), new File(tempRepo, "lib/test-load-balancer.jar"));
        FileUtil.copyFile(new File("lib/production"), new File(tempRepo, "lib"));


        bombIf(runCommand(tempRepo, "hg", "init") != 0, "hg init failed!");
        bombIf(runCommand(tempRepo, "hg", "addremove") != 0, "hg addremove failed!");
        bombIf(runCommand(tempRepo, "hg", "ci", "-m", "Initial checkin") != 0, "hg ci failed");
        return tempRepo;
    }
}
