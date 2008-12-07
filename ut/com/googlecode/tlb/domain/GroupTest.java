package com.googlecode.tlb.domain;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class GroupTest {
    @Test
    public void shouldReturnJobIndex() {
        assertThat(new Group("job1", "job2", "job3").jobIndex("job1"), is(1));
        assertThat(new Group("job1", "job2", "job3").jobIndex("job2"), is(2));
        assertThat(new Group("job1", "job2", "job3").jobIndex("job3"), is(3));
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionIfJobDoesNotExist() throws Exception {
        new Group("job1", "job2", "job3").jobIndex("job4");
    }
}



