package com.googlecode.tlb;

import com.googlecode.tlb.utils.SystemUtil;
import org.hamcrest.core.Is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JunitSupportLocalGroupLoaderTest {
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
