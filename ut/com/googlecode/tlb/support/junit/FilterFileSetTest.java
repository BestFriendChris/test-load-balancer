package com.googlecode.tlb.support.junit;

import com.googlecode.tlb.utils.FileUtil;
import com.googlecode.tlb.testhelpers.CurrentJobMother;
import static com.googlecode.tlb.testhelpers.CruiseConnectorMother.connectorStub;
import com.googlecode.tlb.support.cruise.AgentBasedGroupLoader;
import com.googlecode.tlb.support.cruise.CurrentJob;
import com.googlecode.tlb.support.twist.TwistLoadBalancer;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResource;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class FilterFileSetTest {

    private static final String manualScenario = "Tags:svn support,customizable reports,scheduling\n" +
            "Preconditions:Completed Svn Pipeline \n" +
            "Metadata:ScenarioType=Manual\n" +
            "\n" +
            "Customize Sub Tab, Scheduling:\n" +
            "#open job detail\n" +
            "#verify sub tab \"My Tab\" not exists\n" +
            "#add customized sub tab \"MyTab\" \n" +
            "#open job detail\n" +
            "#verify sub tab \"MyTab\" exists\n" +
            "#verify sub tab \"MyTab\" content exist\n" +
            "#trigger new pipeline and complete it\n" +
            "#open job detail\n" +
            "#verify sub tab \"MyTab\" exists\n" +
            "#verify sub tab \"MyTab\" content exist";

    private File temp = null;
    private FilterFileSet filter;

    @Before
    public void setUp() throws Exception {
        temp = FileUtil.createTempFolder();
    }

    @After
    public void tearDown() throws Exception {
        temp.delete();
    }

    @Test
    public void shouldFilterFile() throws IOException {
        final CurrentJob job = CurrentJobMother.currentJobStub("buildPlan-1", "dev", "evolve");
        filter = new FilterFileSet(new AgentBasedGroupLoader(connectorStub(), job), new JUnitLoadBalancer());
        File file = FileUtil.createFileInFolder(temp, "asdf");
        filter.setDir(temp);
        filter.setProject(new Project());

        Iterator iterator = filter.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat((FileResource) iterator.next(), is(new FileResource(file)));
    }

    @Test
    public void shouldFilterManualScenarioForTwist() throws IOException {
        final CurrentJob job = CurrentJobMother.currentJobStub("buildPlan-1", "dev", "evolve");
        filter = new FilterFileSet(new AgentBasedGroupLoader(connectorStub(), job), new TwistLoadBalancer());
        File file = FileUtil.createFileInFolder(temp, "asdf.scn");
        writeStringToFile(file, manualScenario);
        filter.setDir(temp);
        filter.setProject(new Project());

        Iterator iterator = filter.iterator();
        assertThat("does not include manual scenarios", iterator.hasNext(), is(false));
    }

    @Test
    public void shouldFilterOutHalfTestResources() throws IOException {
        final CurrentJob job = CurrentJobMother.currentJobStub("windows-1", "dev", "evolve");
        filter = new FilterFileSet(new AgentBasedGroupLoader(connectorStub(), job), new JUnitLoadBalancer());
        File file1 = FileUtil.createFileInFolder(temp, "file1");
        FileUtil.createFileInFolder(temp, "file2");

        filter.setDir(temp);
        filter.setProject(new Project());


        Iterator iterator = filter.iterator();

        assertThat(iterator.hasNext(), is(true));
        FileResource fileResource = (FileResource) iterator.next();
        assertThat(fileResource.getFile(), is(file1));
        assertThat(iterator.hasNext(), is(false));
    }


}
