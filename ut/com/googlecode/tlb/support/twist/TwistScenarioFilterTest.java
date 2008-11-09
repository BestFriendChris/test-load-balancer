package com.googlecode.tlb.support.twist;

import com.googlecode.tlb.domain.Range;
import com.googlecode.tlb.utils.FileUtil;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwistScenarioFilterTest {
    private File scenarioDir;
    private List<File> scenarioFiles;
    private File scn1;
    private File scn2;
    private File scn3;

    @Before
    public void setup() throws IOException {
        scenarioDir = FileUtil.createTempFolder();
        scn1 = FileUtil.createFileInFolder(scenarioDir, "1.scn");
        scn2 = FileUtil.createFileInFolder(scenarioDir, "2.scn");
        scn3 = FileUtil.createFileInFolder(scenarioDir, "3.scn");
        scenarioFiles = Arrays.asList(scenarioDir.listFiles());
    }

    @Test
    public void shouldSortScenariosAccordingToFileName() {
        assertThat(scenarioDir.listFiles().length, is(3));

        new TwistScenarioFilter(scenarioFiles).filter(new Range(0, 1));

        List<File> files = Arrays.asList(scenarioDir.listFiles());
        assertThat(files.size(), is(1));
        assertThat(files.get(0), is(scn1));
        assertThat(files.get(0), is(not(scn3)));
    }

    @Test
    public void shouldFilterScenariosAccordingToRange() {
        assertThat(scenarioDir.listFiles().length, is(3));

        new TwistScenarioFilter(scenarioFiles).filter(new Range(1, 2));

        List<File> files = Arrays.asList(scenarioDir.listFiles());
        assertThat(files.size(), is(2));
        assertThat(files.contains(scn2), is(true));
        assertThat(files.contains(scn3), is(true));
    }

    @Test
    public void shouldContinueToDeleteFileEvenIfAnyFileIsFailedToDelete() throws Exception {
        final TwistScenarioFilter scenarioFilter = new TwistScenarioFilter(scenarioFiles);
        final DeletableFile file = new DeletableFile("1");
        final FailedToDeleteFile file2 = new FailedToDeleteFile("2");
        final DeletableFile file3 = new DeletableFile("3");
        final List<File> files = Arrays.asList(file, file2, file3);
        
        scenarioFilter.deleteScenarios(files, new ArrayList<File>());
        assertThat(file.isDeleted(), is(true));
        assertThat(file3.isDeleted(), is(true));
    }

    class DeletableFile extends File {
        private boolean deleted = false;

        public DeletableFile(String pathname) {
            super(pathname);
        }

        public boolean delete() {
            this.deleted = true;
            return this.deleted;
        }

        public boolean isDeleted() {
            return deleted;
        }
    }

    class FailedToDeleteFile extends File {
        FailedToDeleteFile(File parent, String child) {
            super(parent, child);
        }

        public FailedToDeleteFile(String pathname) {
            super(pathname);
        }

        public boolean delete() {
            throw new RuntimeException("failed to delete file");
        }
    }
}



