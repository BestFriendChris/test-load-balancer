package com.googlecode.tlb.support.twist;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import com.googlecode.tlb.utils.FileUtil;
import com.googlecode.tlb.domain.LoadBalancer;
import static com.googlecode.tlb.support.twist.TwistLoadBalancer.getJobName;

import java.io.File;
import java.util.UUID;

public class TwistLoadBalancerTest {
    private File scenarioDir;
    private File scn1;
    private File scn2;
    private File scn3;

    @Before
    public void setUp() throws Exception {
        System.setProperty(TwistLoadBalancer.CRUISE_JOB_NAME, "job1");
        scenarioDir = FileUtil.createTempFolder(UUID.randomUUID().toString());
        scn1 = FileUtil.createFileInFolder(scenarioDir, "1.scn");
        scn2 = FileUtil.createFileInFolder(scenarioDir, "2.scn");
        scn3 = FileUtil.createFileInFolder(scenarioDir, "3.scn");
    }

    @After
    public void tearDown() throws Exception {
        System.clearProperty(TwistLoadBalancer.CRUISE_JOB_NAME);
    }

    @Test
    public void end2endWithJobsEvenlyDevided() throws Exception {
        assertThat(scenarioDir.listFiles().length, is(3));
        LoadBalancer.getLoadBalancer(1, 2).balance(scenarioDir);

        final File[] filesToKeep = scenarioDir.listFiles();
        assertThat(filesToKeep.length, is(1));
        assertThat(filesToKeep[0], is(scn1));
    }

    @Test
    public void shouldUseEnvironmentVairableWhenSystemPropertyIsEmpty() throws Exception {
        System.clearProperty(TwistLoadBalancer.CRUISE_JOB_NAME);
        final String job = getJobName("jobNameFromEnv");
        assertThat(job, is("jobNameFromEnv"));
    }

    @Test
    public void shouldOverideEnvironmentVairableWithSystemProperty() throws Exception {
        final String job = getJobName("jobNameFromEnv");
        assertThat(job, is("job1"));
    }

    @Test
    public void end2endWhenJobsCanNotBeEvenlyDevided() throws Exception {
        System.setProperty(TwistLoadBalancer.CRUISE_JOB_NAME, "job2");

        assertThat(scenarioDir.listFiles().length, is(3));

        LoadBalancer.getLoadBalancer(2, 2).balance(scenarioDir);

        final File[] filesToKeep = scenarioDir.listFiles();
        assertThat(filesToKeep.length, is(2));
        assertThat(filesToKeep[0], is(scn2));
        assertThat(filesToKeep[1], is(scn3));

    }
}
