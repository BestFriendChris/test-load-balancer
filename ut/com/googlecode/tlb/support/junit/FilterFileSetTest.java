package com.googlecode.tlb.support.junit;

import com.googlecode.tlb.utils.FileUtil;
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
    public static final String CRUISE_JOB_NAME = "cruise.job.name";
    private File temp = null;

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
        FilterFileSet filter = new FilterFileSet();
        File file = FileUtil.createFileInFolder(temp, "asdf");
        filter.setDir(temp);
        filter.setProject(new Project());
        filter.setLoadBalance("[job1]");
        System.setProperty(CRUISE_JOB_NAME, "job1");

        Iterator iterator = filter.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat((FileResource) iterator.next(), is(new FileResource(file)));
    }

    @Test
    public void shouldFilterOutHalfTestResources() throws IOException {
        FilterFileSet filter = new FilterFileSet();
        File file1 = FileUtil.createFileInFolder(temp, "file1");
        FileUtil.createFileInFolder(temp, "file2");

        filter.setDir(temp);
        filter.setProject(new Project());
        filter.setLoadBalance("[job1, job2]");
        System.setProperty(CRUISE_JOB_NAME, "job1");

        Iterator iterator = filter.iterator();

        assertThat(iterator.hasNext(), is(true));
        FileResource fileResource = (FileResource) iterator.next();
        assertThat(fileResource.getFile(), is(file1));
        assertThat(iterator.hasNext(), is(false));
    }


}
