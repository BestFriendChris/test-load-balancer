package com.googlecode.tlb;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.hamcrest.core.Is;

import java.io.*;
import java.util.Map;
import java.util.HashMap;

import static com.googlecode.tlb.support.twist.TwistLoadBalancer.JOBNAME;

public class TwistSupportFunctionalTest {

    @Before
    public void setup() throws Exception {
        File reports = new File("ft/twist/tlb-twist/target/reports");
        reports.delete();
        reports.mkdirs();
        if (!new File("target/test-load-balancer.jar").exists()) {
            runCommand(new HashMap(), new File("."), new String[]{"ant", "jar.module.test-load-balancer"});
        }
        runCommand(new HashMap(), new File("ft/twist/tlb-twist"), new String[]{"ant", "retrieve"});


    }

    @After
    public void teardown() throws Exception {
        runCommand(new HashMap(), new File("ft/twist/tlb-twist/scenarios"), new String[]{"svn", "revert", "-R", "."});
    }

    @Test
    public void shouldRunFirstTestWhenAgentIsRunningAsJob1() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(JOBNAME, "job1");
        runCommand(hashMap, new File("ft/twist/tlb-twist"), new String[]{"ant", "twist"});

        File reports = new File("ft/twist/tlb-twist/target/reports");
        int count = reportCount(reports);
        assertThat(count, Is.is(1));

    }

    @Test
    public void shouldRunLast2TestWhenAgentIsRunningAsJob2() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(JOBNAME, "job2");
        runCommand(hashMap, new File("ft/twist/tlb-twist"), new String[]{"ant", "twist"});

        File reports = new File("ft/twist/tlb-twist/target/reports");
        int count = reportCount(reports);
        assertThat(count, Is.is(2));
    }

    @Test
    public void shouldRunAllTestsWhenThereIsNoJobSpecified() throws Exception {
        HashMap map = new HashMap();
        map.put(JOBNAME, "");
        runCommand(map, new File("ft/twist/tlb-twist"), new String[]{"ant", "twist"});

        File reports = new File("ft/twist/tlb-twist/target/reports");
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
