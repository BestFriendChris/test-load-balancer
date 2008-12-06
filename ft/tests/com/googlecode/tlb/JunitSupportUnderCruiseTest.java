package com.googlecode.tlb;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertThat;
import org.hamcrest.core.Is;

import java.util.HashMap;

import static com.googlecode.tlb.JunitSupportFunctionalTest.runAntCommand;
import static com.googlecode.tlb.JunitSupportFunctionalTest.before;
import static com.googlecode.tlb.JunitSupportFunctionalTest.reportsCount;
import static com.googlecode.tlb.JunitSupportFunctionalTest.workingFolder;
import com.googlecode.tlb.support.cruise.CurrentJob;
import com.googlecode.tlb.support.cruise.CruiseAgentSession;

//@Precondition Cruise Server Is Running
@Ignore
public class JunitSupportUnderCruiseTest {
    public HashMap<String, String> env;

    @Before
    public void setUp() throws Exception {
        before();
        env = new HashMap<String, String>();
        env.put(CruiseAgentSession.SERVER_URL_KEY, "https://localhost:8154/cruise");
        env.put(CurrentJob.PIPELINE_NAME_KEY, "connectfour");
        env.put(CurrentJob.STAGE_NAME_KEY, "defaultStage");
    }
    
    @Test
    public void shouldRunFirstTwoTestsWhenAgentIsRunningAsJob1() throws Exception {
        env.put(CurrentJob.JOB_NAME_KEY, "job-1");
        runAntCommand(env, workingFolder);

        assertThat(reportsCount(), Is.is(2));
    }

    @Test
    public void shouldRunFirstTwoTestsWhenAgentIsRunningAsJob2() throws Exception {
        env.put(CurrentJob.JOB_NAME_KEY, "job-2");
        runAntCommand(env, workingFolder);

        assertThat(reportsCount(), Is.is(1));
    }

    @Test
    public void shouldRunAllTestsWhenThereIsNoJobSpecified() throws Exception {
        runAntCommand(env, workingFolder);

        assertThat(reportsCount(), Is.is(3));
    }

}
