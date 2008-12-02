package com.googlecode.tlb.support.twist;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertThat;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.hamcrest.core.Is;
import static org.hamcrest.core.Is.is;
import org.hamcrest.Matcher;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import com.googlecode.tlb.utils.FileUtil;

public class FilteredFileTest {
    private File scenarioDir;

    @Before
    public void setUp() throws Exception {
        scenarioDir = FileUtil.createTempFolder();
        FileUtil.createFileInFolder(scenarioDir, "1.scn");
        FileUtil.createFileInFolder(scenarioDir, "2.scn");
        FileUtil.createFileInFolder(scenarioDir, "3.scn");
        FileUtil.createFileInFolder(scenarioDir, "4.java");
    }

    @Test
    public void shouldListFilesBasedOnFileSet() {
        FileSet set = new FileSet();
        Project project = new Project();
        set.setProject(project);
        DirectoryScanner scanner = new DirectoryScanner();

        scanner.setBasedir(scenarioDir);
        set.setupDirectoryScanner(scanner, project);
        set.setIncludes("*.scn");
        set.setDir(scenarioDir);


        FilteredFile filteredFile = new FilteredFile(scenarioDir, set);
        File[] children = filteredFile.listFiles((FileFilter) null);
        assertThat(children.length, is(3));
        assertThat(children[0].getName().endsWith(".scn"), is(true));
        assertThat(children[1].getName().endsWith(".scn"), is(true));
        assertThat(children[2].getName().endsWith(".scn"), is(true));
    }




}
