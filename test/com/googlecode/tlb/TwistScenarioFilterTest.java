package com.googlecode.tlb;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;

import com.googlecode.tlb.utils.FileUtil;
import static junit.framework.Assert.fail;

public class TwistScenarioFilterTest {
    private File scenarioDir;
    private File file1;

    @Before
    public void setup() throws IOException {
        scenarioDir = FileUtil.createTempFolder("scenarios");
        file1 = FileUtil.createFileInFolder(scenarioDir, "1.scn");
        FileUtil.createFileInFolder(scenarioDir, "2.scn");
        FileUtil.createFileInFolder(scenarioDir, "3.scn");
    }

    @Test
    public void shouldFilterScenariosAccordingToFactor() {
        assertThat(scenarioDir.listFiles().length, is(3));

        new TwistScenarioFilter(scenarioDir).filter(new LoadBalanceFactor(1, 3));

        File[] files = scenarioDir.listFiles();
        assertThat(files.length, is(1));
        assertThat(files[0].getName(), is("1.scn"));
    }

    @Test
    public void shouldAssignTheRestToTheLastAgentIfLoadCanNotBeEvenlyBalanced() throws Exception {
        assertThat(scenarioDir.listFiles().length, is(3));

        new TwistScenarioFilter(scenarioDir).filter(new LoadBalanceFactor(2, 2));

        File[] files = scenarioDir.listFiles();
        assertThat(files.length, is(2));
        assertThat(files[0].getName(), is("2.scn"));
        assertThat(files[1].getName(), is("3.scn"));
    }

    @Test
    public void shouldThrowExceptionIfScenarioDirDoesNotExist() throws Exception {
        File invalidDir = new File("invalid");
        try {
            new TwistScenarioFilter(invalidDir).filter(new LoadBalanceFactor(2, 2));
            fail("should have thrown RuntimeException instead");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldContinueToDeleteFileEvenIfAnyFileIsFailedToDelete() throws Exception {
        final TwistScenarioFilter scenarioFilter = new TwistScenarioFilter(scenarioDir);
        final DeletableFile file = new DeletableFile("1");
        final FailedToDeleteFile file2 = new FailedToDeleteFile("2");
        final DeletableFile file3 = new DeletableFile("3");
        final File[] files = {file, file2, file3};
        
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



