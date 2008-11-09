package com.googlecode.tlb;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.hamcrest.core.Is;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import static com.googlecode.tlb.support.twist.TwistLoadBalancerTask.JOBNAME;

public class JunitSupportFunctionalTest {

    @Before
    public void setup() throws Exception {
        File reports = new File("ft/junit/connectfour/target/test-results");
        reports.delete();
        reports.mkdirs();
        if (!new File("target/test-load-balancer.jar").exists()) {
            runCommand(new HashMap(), new File("."), "ant", "jar.module.test-load-balancer");
        }
        runCommand(new HashMap(), new File("ft/junit/connectfour"), "ant", "retrieve");
    }

    @After
    public void teardown() throws Exception {
    }

    @Test
    public void shouldRunFirstTwoTestsWhenAgentIsRunningAsJob1() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(JOBNAME, "job1");
        runCommand(hashMap, new File("ft/junit/connectfour"), "ant");

        File reports = new File("ft/junit/connectfour/target/test-results");
        int count = reportCount(reports);
        assertThat(count, Is.is(2));

    }

    @Test
    public void shouldRunLast1TestWhenAgentIsRunningAsJob2() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(JOBNAME, "job2");
        runCommand(hashMap, new File("ft/junit/connectfour"), new String[]{"ant"});

        File reports = new File("ft/junit/connectfour/target/test-results");
        int count = reportCount(reports);
        assertThat(count, Is.is(1));
    }

    @Test
    public void shouldRunAllTestsWhenThereIsNoJobSpecified() throws Exception {
        HashMap map = new HashMap();
        map.put(JOBNAME, "");
        runCommand(map, new File("ft/junit/connectfour"), new String[]{"ant"});

        File reports = new File("ft/junit/connectfour/target/test-results");
        int count = reportCount(reports);
        assertThat(count, Is.is(3));

    }

    private int runCommand(Map envMap, File directory, String... command) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(command);
        Map<String, String> env = pb.environment();
        env.putAll(envMap);
        pb.directory(directory);
        Process p = pb.start();
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String current = "";
        while ((current = inputStream.readLine()) != null) {
            System.out.println(current);
        }
        p.waitFor();
        return p.exitValue();

    }

    private int reportCount(File reports) {
        FilenameFilter filter = new XMLFilter();
        String[] files = reports.list(filter);
        return files.length;
    }

    static class XMLFilter implements FilenameFilter {
        public boolean accept(File file, String name) {
            return name.toLowerCase().endsWith(".xml");
        }
    }
}
