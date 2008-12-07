package com.googlecode.tlb;

import org.junit.Test;
import org.junit.Before;

import java.io.File;
import java.util.UUID;


public class JunitSupportForCruiseWithSecurityTest extends SupportForCruiseWithSecurityTest {
    @Before
    public void setUp() throws Exception {
        File hgRepo = createHgRepo("connectfour", "ft/junit/connectfour");
        pipeline = UUID.randomUUID().toString();
        serverIsRunning = new ServerIsRunning(hgRepo.getAbsolutePath(), pipeline);
        agentIsRunning = new AgentIsRunning();
        serverIsRunning.start();
        agentIsRunning.start();
    }

    @Test
    public void shouldRunUnderCruise() throws Exception {
        Thread.sleep(90000);
        assertJobTestCount("job-1", 11);
        assertJobTestCount("job-2", 10);
    }
}
