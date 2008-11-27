package com.googlecode.tlb.support.cruise;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static com.googlecode.tlb.support.cruise.GroupsDivider.divid;
import com.googlecode.tlb.support.twist.Group;

import java.util.List;
import java.util.ArrayList;

public class GroupsDividerTest {
    @Test
    public void shouldDivBaseOnPrefix() throws Exception {
        List<String> allJobs = new ArrayList<String>();
        allJobs.add("windows-1");
        allJobs.add("windows-2");
        allJobs.add("windows-3");
        allJobs.add("linux-1");
        allJobs.add("linux-2");
        allJobs.add("linux-3");
        Group group = divid(allJobs, "windows-1");
        assertThat(group.jobsCount(), is(3));
        assertThat(group.jobAt(0), is("windows-1"));
        assertThat(group.jobAt(1), is("windows-2"));
        assertThat(group.jobAt(2), is("windows-3"));
    }

    @Test
    public void shouldDivGroupWhenTheJobNameIsPrefix() throws Exception {
        List<String> allJobs = new ArrayList<String>();
        allJobs.add("windows");
        allJobs.add("windows-2");
        allJobs.add("windows-3");
        allJobs.add("linux-1");
        allJobs.add("linux-2");
        allJobs.add("linux-3");
        Group group = divid(allJobs, "windows-1");
        assertThat(group.jobsCount(), is(3));
        assertThat(group.jobAt(0), is("windows"));
        assertThat(group.jobAt(1), is("windows-2"));
        assertThat(group.jobAt(2), is("windows-3"));
    }


    @Test
    public void dashShouldBePartOfConvention() throws Exception {
        List<String> allJobs = new ArrayList<String>();
        allJobs.add("windows");
        allJobs.add("window-2");
        allJobs.add("window-3");
        allJobs.add("linux-1");
        allJobs.add("linux-2");
        allJobs.add("linux-3");
        Group windowsGroup = divid(allJobs, "windows");
        assertThat(windowsGroup.jobsCount(), is(1));
        Group winGroup = divid(allJobs, "window-2");
        assertThat(winGroup.jobsCount(), is(2));
        Group linuxGroup = divid(allJobs, "linux-1");
        assertThat(linuxGroup.jobsCount(), is(3));
    }

}
