package com.googlecode.tlb.domain;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import com.googlecode.tlb.domain.Group;
import com.googlecode.tlb.exceptions.JobNotFoundException;

public class LocalGroupLoaderTest {

    @Test
    public void shouldReturnGroupWith1Job() throws JobNotFoundException {
        LocalGroupLoader loader = new LocalGroupLoader();
        Group group = loader.load();
        assertThat(group.jobsCount(), is(1));
        assertThat(group.jobIndex(), is(0));
    }
}
