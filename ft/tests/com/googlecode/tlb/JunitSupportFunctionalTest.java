package com.googlecode.tlb;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.hamcrest.core.Is;
import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import static com.googlecode.tlb.support.twist.TwistLoadBalancerTask.JOBNAME;
import com.googlecode.tlb.support.cruise.LocalGroupLoader;

public class JunitSupportFunctionalTest {
    private static final Logger LOG = Logger.getLogger(JunitSupportFunctionalTest.class);
    static File reportsFolder = new File("ft/junit/connectfour/target/test-results");
    static File workingFolder = new File("ft/junit/connectfour");

    @Before
    public void setUp() throws Exception {
        before();
    }

    static void before() throws Exception {
        reportsFolder.delete();
        reportsFolder.mkdirs();

        if (!new File("target/test-load-balancer.jar").exists()) {
            runAntCommand(new HashMap(), new File("."), "jar.module.test-load-balancer");
        }
    }

    @Test
    public void shouldRunFirstTwoTestsWhenAgentIsRunningAsJob1() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(LocalGroupLoader.PIECES, "2");
        hashMap.put(LocalGroupLoader.INDEX, "1");
        runAntCommand(hashMap, workingFolder);

        assertThat(reportsCount(), Is.is(2));
    }

    @Test
    public void shouldRunLast1TestWhenAgentIsRunningAsJob2() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(LocalGroupLoader.PIECES, "2");
        hashMap.put(LocalGroupLoader.INDEX, "2");
        runAntCommand(hashMap, workingFolder);

        assertThat(reportsCount(), Is.is(1));
    }

    @Test
    public void shouldRunAllTestsWhenThereIsNoJobSpecified() throws Exception {
        HashMap map = new HashMap();
        map.put(JOBNAME, "");
        runAntCommand(map, workingFolder);

        assertThat(reportsCount(), Is.is(3));
    }

    static int runAntCommand(Map<String, String> envMap, File directory, String... args) throws Exception {
        ArrayList<String> cmd = new ArrayList<String>();
        cmd.add(antCommand());
        cmd.addAll(Arrays.asList(args));

        ProcessBuilder pb = new ProcessBuilder(cmd);
        Map<String, String> env = pb.environment();
        env.putAll(envMap);
        pb.directory(directory);
        Process p = pb.start();
        logProcessOutput(p.getInputStream());
        p.waitFor();
        return p.exitValue();
    }

    private static void logProcessOutput(InputStream processStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(processStream));
        String line = "";
        while ((line = reader.readLine()) != null) {
            LOG.info(line);
        }
        IOUtils.closeQuietly(processStream);
    }

    static int reportsCount() {
        FilenameFilter filter = new XMLFilter();
        String[] files = reportsFolder.list(filter);
        return files.length;
    }

    static class XMLFilter implements FilenameFilter {
        public boolean accept(File file, String name) {
            return name.toLowerCase().endsWith(".xml");
        }
    }

    public static String antCommand() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return "ant.bat";
        } else {
            return "ant";
        }
    }
}
