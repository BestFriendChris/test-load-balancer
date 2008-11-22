package com.googlecode.tlb.support.junit;

import com.googlecode.tlb.utils.FileUtil;
import com.googlecode.tlb.testhelpers.CruiseConnectorMother;
import com.googlecode.tlb.testhelpers.CurrentJobMother;
import static com.googlecode.tlb.testhelpers.CruiseConnectorMother.connectorStub;
import com.googlecode.tlb.support.cruise.GroupLoader;
import com.googlecode.tlb.support.cruise.AgentBasedGroupLoader;
import com.googlecode.tlb.domain.CurrentJob;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class FilterFileSetTest {
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
        filter = new FilterFileSet(new AgentBasedGroupLoader(connectorStub(), job), job);
        File file = FileUtil.createFileInFolder(temp, "asdf");
        filter.setDir(temp);
        filter.setProject(new Project());

        Iterator iterator = filter.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat((FileResource) iterator.next(), is(new FileResource(file)));
    }

    @Test
    public void shouldFilterOutHalfTestResources() throws IOException {
        final CurrentJob job = CurrentJobMother.currentJobStub("windows-1", "dev", "evolve");
        filter = new FilterFileSet(new AgentBasedGroupLoader(connectorStub(), job), job);
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
