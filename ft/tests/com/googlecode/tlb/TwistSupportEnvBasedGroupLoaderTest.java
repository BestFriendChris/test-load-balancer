package com.googlecode.tlb;

import com.googlecode.tlb.utils.SystemUtil;
import com.googlecode.tlb.support.cruise.EnvBasedGroupLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import org.hamcrest.core.Is;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TwistSupportEnvBasedGroupLoaderTest {

    @Before
    public void setup() throws Exception {
        File reports = new File("ft/twist/tlb-twist/target/reports");
        reports.delete();
        reports.mkdirs();

        File jar = new File("target/test-load-balancer.jar");
        if (!jar.exists()) {
            runCommand(new HashMap(), new File("."), ant(), "jar.module.test-load-balancer");
        }
        runCommand(new HashMap(), new File("ft/twist/tlb-twist"), ant(), "retrieve");


    }

    private String ant() {
        return SystemUtil.antCommand();
    }

    @After
    public void teardown() throws Exception {
        // TODO - yanghada - keeping both commands here until we all migrate to git 
//        runCommand(new HashMap(), new File("ft/twist/tlb-twist/scenarios"), "svn", "revert", "-R", ".");
//        runCommand(new HashMap(), new File("ft/twist/tlb-twist/scenarios"), "git", "checkout", ".");
    }

    @Test
    public void shouldRunFirst2TestsWhenAgentIsRunningAsJob1() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(EnvBasedGroupLoader.PIECES, "2");
        hashMap.put(EnvBasedGroupLoader.INDEX, "1");
        runCommand(hashMap, new File("ft/twist/tlb-twist"), ant(), "twist");

        File reports = new File("ft/twist/tlb-twist/target/reports");
        int count = reportCount(reports);
        assertThat(count, Is.is(2));

    }

    @Test
    public void shouldRunLast1TestWhenAgentIsRunningAsJob2() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(EnvBasedGroupLoader.PIECES, "2");
        hashMap.put(EnvBasedGroupLoader.INDEX, "2");

        runCommand(hashMap, new File("ft/twist/tlb-twist"), ant(), "twist");

        File reports = new File("ft/twist/tlb-twist/target/reports");
        int count = reportCount(reports);
        assertThat(count, Is.is(1));
    }

    @Test
    public void shouldRunAllTestsWhenThereIsNoJobSpecified() throws Exception {
        HashMap map = new HashMap();

        runCommand(map, new File("ft/twist/tlb-twist"), ant(), "twist");

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
        String current;
        while ((current = inputStream.readLine()) != null) {
            System.out.println(current);
        }
        p.waitFor();
        return p.exitValue();

    }

    private int reportCount(File reports) {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File file, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        };
        String[] files = reports.list(filter);
        return files.length;
    }

}
