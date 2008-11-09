package com.googlecode.tlb.support.twist;

import static com.googlecode.tlb.support.twist.TwistLoadBalancerTask.getJobName;
import com.googlecode.tlb.utils.FileUtil;
import static org.hamcrest.core.Is.is;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TwistLoadBalancerTest {
    private File scenarioDir;
    private File scn1;
    private File scn2;
    private File scn3;
    private Iterator iterator;

    @Before
    public void setUp() throws Exception {
        System.setProperty(TwistLoadBalancerTask.CRUISE_JOB_NAME, "job1");
        scenarioDir = FileUtil.createTempFolder();
        scn1 = FileUtil.createFileInFolder(scenarioDir, "1.scn");
        scn2 = FileUtil.createFileInFolder(scenarioDir, "2.scn");
        scn3 = FileUtil.createFileInFolder(scenarioDir, "3.scn");
        iterator = Arrays.asList(scenarioDir.listFiles()).iterator();
    }

    @After
    public void tearDown() throws Exception {
        System.clearProperty(TwistLoadBalancerTask.CRUISE_JOB_NAME);
    }

    @Test
    public void end2endWithJobsEvenlyDevided() throws Exception {
        assertThat(scenarioDir.listFiles().length, is(3));
        new TwistLoadBalancer().balance(iterator, 2, 1);

        final File[] filesToKeep = scenarioDir.listFiles();
        List<File> filesToKeepList = Arrays.asList(filesToKeep);
        assertThat(filesToKeepList.size(), is(2));
        assertThat(filesToKeepList.contains(scn1), is(true));
        assertThat(filesToKeepList.contains(scn2), is(true));
    }

    @Test
    public void shouldUseEnvironmentVairableWhenSystemPropertyIsEmpty() throws Exception {
        System.clearProperty(TwistLoadBalancerTask.CRUISE_JOB_NAME);
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
        System.setProperty(TwistLoadBalancerTask.CRUISE_JOB_NAME, "job2");

        assertThat(scenarioDir.listFiles().length, is(3));

        new TwistLoadBalancer().balance(iterator, 2, 2);

        final File[] filesToKeep = scenarioDir.listFiles();
        assertThat(filesToKeep.length, is(1));
        assertThat(filesToKeep[0], is(scn3));
    }
}
