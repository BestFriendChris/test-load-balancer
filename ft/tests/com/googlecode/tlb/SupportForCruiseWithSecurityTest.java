package com.googlecode.tlb;

import org.junit.After;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.Assert.assertThat;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.util.Properties;
import java.util.UUID;

import com.googlecode.tlb.utils.FileUtil;

public class SupportForCruiseWithSecurityTest {
    protected ServerIsRunning serverIsRunning;
    protected AgentIsRunning agentIsRunning;
    protected String pipeline;

    public void setUp(File hgRepo) throws Exception {
        prepareCruiseServerAndAgent();

        this.pipeline = UUID.randomUUID().toString();

        serverIsRunning = new ServerIsRunning(hgRepo.getAbsolutePath(), pipeline);
        agentIsRunning = new AgentIsRunning();
        serverIsRunning.start();
        agentIsRunning.start();
    }

    private void prepareCruiseServerAndAgent() throws IOException {
        Properties properties = new Properties();
        File file = new File("ft/integration/cruise.properties");
        if (!file.exists()) {
            String messages = "Please create cruise.properties file under your ft/integration folder\n\n"
                    + "cruise.properties should be look like this:\n"
                    + "cruise.server = ABSOLUTE PATH TO YOUR cruise-server-1.1 FOLDER\n"
                    + "cruise.agent = ABSOLUTE PATH TO YOUR cruise-agent-1.1 FOLDER\n"
                    + "";
            throw new RuntimeException(messages);
        }
        properties.load(new FileReader(file));

        prepare(properties.getProperty("cruise.server").trim(), "server.sh", "stop-server.sh");
        prepare(properties.getProperty("cruise.agent").trim(), "agent.sh", "stop-agent.sh");
    }

    void prepare(String dir, String... shells) throws IOException {
        File file = new File(dir);
        if (!file.exists()) {
            throw new RuntimeException("Please check the directory you defined in your cruise.properties exist or not\n"
                    + "[" + file.getAbsolutePath() + "] can not be found");
        }
        File integration = new File("ft/integration");
        File cruise = new File(integration, file.getName());
        if (cruise.exists()) {
            cruise.delete();
        }

        FileUtils.copyDirectoryToDirectory(file, integration);
        for (String shell : shells) {
            new File("ft/integration/" + file.getName() + "/" + shell).setExecutable(true);
        }

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
