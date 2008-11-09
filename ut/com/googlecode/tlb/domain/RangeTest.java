package com.googlecode.tlb.domain;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Arrays;

import com.googlecode.tlb.domain.Range;

public class RangeTest {
    @Test
    public void shouldReturnFilesInRange() {
        final File[] all = { new File("file1"), new File("file2"), new File("file3") };
        final List inRange = new Range(0, 2).in(Arrays.asList(all));

        assertThat(inRange.size(), is(2));
        assertThat((File) inRange.get(0), is(new File("file1")));
        assertThat((File) inRange.get(1), is(new File("file2")));

    }
}



