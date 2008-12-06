package com.googlecode.tlb;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertThat;
import org.hamcrest.core.Is;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import com.googlecode.tlb.support.cruise.EnvBasedGroupLoader;
import com.googlecode.tlb.utils.SystemUtil;

public class JunitSupportFunctionalTest {

    static File reportsFolder = new File("ft/junit/connectfour/target/test-results");
    static File workingFolder = new File("ft/junit/connectfour");

    @Before
    public void setUp() throws Exception {
        before();
    }

    static void before() throws Exception {
        reportsFolder.delete();
        reportsFolder.mkdirs();

        if (!new File("target/test-load-balancer.jar").exists()
                || !new File("ft/junit/connectfour/lib/tlb/test-load-balancer.jar").exists()) {
            runAntCommand(new HashMap(), new File("."), "publish");
        }
    }

    @Test
    public void shouldRunFirstTwoTestsWhenAgentIsRunningAsJob1() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(EnvBasedGroupLoader.PIECES, "2");
        hashMap.put(EnvBasedGroupLoader.INDEX, "1");
        runAntCommand(hashMap, workingFolder);

        assertThat(reportsCount(), Is.is(2));
    }

    @Test
    public void shouldRunLast1TestWhenAgentIsRunningAsJob2() throws Exception {
        HashMap hashMap = new HashMap();
        hashMap.put(EnvBasedGroupLoader.PIECES, "2");
        hashMap.put(EnvBasedGroupLoader.INDEX, "2");
        runAntCommand(hashMap, workingFolder);

        assertThat(reportsCount(), Is.is(1));
    }

    @Test
    public void shouldRunAllTestsWhenThereIsNoJobSpecified() throws Exception {
        HashMap map = new HashMap();
        runAntCommand(map, workingFolder);

        assertThat(reportsCount(), Is.is(3));
    }

    static int runAntCommand(Map<String, String> envMap, File directory, String... args) throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        list.add(SystemUtil.antCommand());
        list.addAll(Arrays.asList(args));
        String[] cmd = list.toArray(new String[list.size()]);

        return SystemUtil.runCommand(envMap, directory, cmd);
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

}
