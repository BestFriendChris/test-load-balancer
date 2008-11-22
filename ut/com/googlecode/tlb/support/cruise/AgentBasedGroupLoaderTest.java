package com.googlecode.tlb.support.cruise;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Before;
import com.googlecode.tlb.support.twist.Group;
import com.googlecode.tlb.support.cruise.GroupLoader;
import com.googlecode.tlb.exceptions.JobNotFoundException;
import com.googlecode.tlb.testhelpers.CurrentJobMother;
import static com.googlecode.tlb.testhelpers.CurrentJobMother.currentJobStub;
import static com.googlecode.tlb.testhelpers.CruiseConnectorMother.connectorStub;

public class AgentBasedGroupLoaderTest {
    private GroupLoader groupLoader;

    @Before
    public void setUp() {
    }

    @Test
    public void shouldFindGroupOnlyContainsMultipleJobs() throws Exception {
        groupLoader = new AgentBasedGroupLoader(connectorStub(), currentJobStub("buildPlan-1", "dev", "evolve"));
        Group group = groupLoader.load();
        assertThat(group.jobsCount(), is(3));
    }

    @Test
    public void shouldFindGroupOnlyContainsOneJob() throws Exception {
        groupLoader = new AgentBasedGroupLoader(connectorStub(), currentJobStub("linux-1", "dev", "evolve"));
        Group group = groupLoader.load();
        assertThat(group.jobsCount(), is(1));
    }

    @Test(expected = JobNotFoundException.class)
    public void shouldThrowExceptionWhenJobNameCannotBeFondAndIsEmpty() throws Exception {
        groupLoader = new AgentBasedGroupLoader(connectorStub(), currentJobStub("", "dev", "evolve"));
        groupLoader.load();
    }


    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenJobNameCannotBeFondAndNotEmpty() throws Exception {
        groupLoader = new AgentBasedGroupLoader(connectorStub(), currentJobStub("no-exist", "dev", "evolve"));
        groupLoader.load();
    }
}
