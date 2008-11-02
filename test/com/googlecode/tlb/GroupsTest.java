package com.googlecode.tlb;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class GroupsTest {
    @Test
    public void shouldReturnGroupContainsCertainJobName() {
        final Group group = new Group("job1", "job2");
        final Groups groups = new Groups(group, new Group("job3", "job4"));
        assertThat(groups.findByJobName("job1"), is(group));
    }


}



