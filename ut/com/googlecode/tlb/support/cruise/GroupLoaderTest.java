package com.googlecode.tlb.support.cruise;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.io.FileUtils;
import com.googlecode.tlb.support.twist.Group;
import com.googlecode.tlb.support.cruise.GroupLoader;
import com.googlecode.tlb.support.cruise.CruiseConnector;
import com.googlecode.tlb.exceptions.JobNotFoundException;

import java.io.InputStream;
import java.io.File;

public class GroupLoaderTest {
    private GroupLoader groupLoader;

    @Before
    public void setUp() {
        groupLoader = new GroupLoader(new CruiseConnector() {
            public String pipelineStatus(String pipelineName, String stageName, String jobName) {
                File file = new File("ut/com/googlecode/tlb/support/cruise/pipelineStatusJson.txt");
                try {
                    return FileUtils.readFileToString(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }

    @Test
    public void shouldFindGroupOnlyContainsMultipleJobs() throws Exception {
        Group group = groupLoader.load("evolve", "dev", "buildPlan-1");
        assertThat(group.jobsCount(), is(3));
    }

    @Test
    public void shouldFindGroupOnlyContainsOneJob() throws Exception {
        Group group = groupLoader.load("evolve", "dev", "linux");
        assertThat(group.jobsCount(), is(1));
    }

    @Test(expected = JobNotFoundException.class)
    public void shouldThrowExceptionWhenJobNameCannotBeFondAndIsEmpty() throws Exception {
        groupLoader.load("evolve", "dev", "");
    }


    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenJobNameCannotBeFondAndNotEmpty() throws Exception {
        groupLoader.load("evolve", "dev", "somethingNotExist");
    }
}
