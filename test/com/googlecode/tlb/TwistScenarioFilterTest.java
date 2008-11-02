package com.googlecode.tlb;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import com.googlecode.tlb.utils.FileUtil;
import static junit.framework.Assert.fail;

public class TwistScenarioFilterTest {
    private File scenarioDir;

    @Before
    public void setup() throws IOException {
        scenarioDir = FileUtil.createTempFolder("scenarios");
        FileUtil.createFileInFolder(scenarioDir, "1.scn");
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
    public void shouldThrowExceptionIfLoadFactorIsInvalid() throws Exception {

    }

    @Test
    public void shouldThrowExceptionIfFilesCanNotBeFiltered() throws Exception {
    }
}



