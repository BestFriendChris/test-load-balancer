package com.googlecode.tlb.support.twist;

import com.googlecode.tlb.domain.GroupLoader;
import com.googlecode.tlb.domain.GroupLoaderFactory;
import com.googlecode.tlb.support.junit.FilterFileSet;
import com.googlecode.tlb.utils.FileUtil;
import com.thoughtworks.twist.core.execution.ant.ScenarioExecutorAntMain;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResource;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ScenarioExecutorAntMainWrapper {
    public static final int PASS_EXIT_CODE = 0;
    public static final int FAILURE_EXIT_CODE = 1;
    public static final int ERROR_EXIT_CODE = 2;
    public static final int FAILURE_AND_ERROR_EXIT_CODE = 3;
    public static final int INVALID_INPUT_EXIT_CODE = 4;

    public static void main(String[] args) throws IOException {
        System.out.println("Start executing twist scenarios.");
        File scenarioDir = new File(args[0]);
        System.out.println("scenarios dir is [" + scenarioDir + "]");
        File reportsDir = new File(args[1]).getAbsoluteFile();
        System.out.println("reports dir is [" + reportsDir + "]");
        String tags = "";
        if (args.length >= 2) {
            tags = args[2];
        }
        System.out.println("tags are [" + tags + "]");
        File tempDir = FileUtil.createTempFolder();

        filter(scenarioDir, tempDir, GroupLoaderFactory.getInstance());
        System.out.println("calling the original ant main");
        ScenarioExecutorAntMain.main(new String[]{tempDir.getAbsolutePath(),
                reportsDir.getAbsolutePath(),
                tags});
        System.out.println("exited the original ant main");
    }

    static void filter(File scenarioDir, File tempFolder, GroupLoader groupLoader) throws IOException {
        FilterFileSet set = new FilterFileSet(groupLoader);
        Project project = new Project();
        project.setBaseDir(scenarioDir.getParentFile());
        set.setDir(scenarioDir);
        set.setIncludes("**/*.scn");
        DirectoryScanner scanner = new DirectoryScanner();

        set.setupDirectoryScanner(scanner, project);
        set.setProject(project);

        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            File src = ((FileResource) iterator.next()).getFile();
            File dest = new File(tempFolder, src.getName());

            FileUtil.copySingleFile(src, dest);
        }

    }
}
