package com.googlecode.tlb;

import static com.googlecode.tlb.utils.ExceptionUtils.bomb;
import static com.googlecode.tlb.utils.ExceptionUtils.bombIf;
import com.googlecode.tlb.utils.FileUtil;
import com.googlecode.tlb.utils.SystemUtil;
import static com.googlecode.tlb.utils.SystemUtil.runCommand;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.FileUtils.copyDirectory;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JunitSupportForCruiseWithSecurityTest {
    private ServerIsRunning serverIsRunning;
    private AgentIsRunning agentIsRunning;

    @Before
    public void setUp() throws Exception {
        File hgRepo = createHgRepo("connectfour");
        serverIsRunning = new ServerIsRunning(hgRepo.getAbsolutePath());
        agentIsRunning = new AgentIsRunning();
        serverIsRunning.start();
        agentIsRunning.start();
    }

    @After
    public void tearDown() throws Exception {
        serverIsRunning.stop();
        agentIsRunning.stop();
    }

    @Test
    public void shouldRunUnderCruise() throws Exception {
        System.out.println("test start");
        Thread.sleep(90000);
        System.out.println("test finished");
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
