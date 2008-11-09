package com.googlecode.tlb.support.twist;

import com.googlecode.tlb.domain.LoadBalanceFactor;
import com.googlecode.tlb.utils.FileUtil;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TwistScenarioFilterTest {
    private File scenarioDir;
    private Iterator<File> iterator;

    @Before
    public void setup() throws IOException {
        scenarioDir = FileUtil.createTempFolder();
        FileUtil.createFileInFolder(scenarioDir, "1.scn");
        FileUtil.createFileInFolder(scenarioDir, "2.scn");
        FileUtil.createFileInFolder(scenarioDir, "3.scn");
        iterator = Arrays.asList(scenarioDir.listFiles()).iterator();
    }

    @Test
    public void shouldFilterScenariosAccordingToFactorWhenTestsCanBeEvenlyDivided() {
        assertThat(scenarioDir.listFiles().length, is(3));

        new TwistScenarioFilter(iterator).filter(new LoadBalanceFactor(1, 3));

        File[] files = scenarioDir.listFiles();
        assertThat(files.length, is(1));
        assertThat(files[0].getName(), is("1.scn"));
    }

    @Test
    public void shouldFilterScenariosAccordingToFactorWhenTestsCanNotBeEvenlyDivided() throws Exception {
        assertThat(scenarioDir.listFiles().length, is(3));

        new TwistScenarioFilter(iterator).filter(new LoadBalanceFactor(2, 2));

        File[] files = scenarioDir.listFiles();
        assertThat(files.length, is(1));
        assertThat(files[0].getName(), is("3.scn"));
    }

    @Test
    public void shouldContinueToDeleteFileEvenIfAnyFileIsFailedToDelete() throws Exception {
        final TwistScenarioFilter scenarioFilter = new TwistScenarioFilter(iterator);
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



