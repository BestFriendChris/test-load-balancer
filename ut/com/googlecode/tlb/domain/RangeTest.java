package com.googlecode.tlb.domain;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class RangeTest {
    @Test
    public void shouldReturnFilesInRange() {
        final File[] all = { new File("file1"), new File("file2"), new File("file3") };
        final List inRange = new Range(0, 2).in(Arrays.asList(all));

        assertThat(inRange.size(), is(2));
        assertThat((File) inRange.get(0), is(new File("file1")));
        assertThat((File) inRange.get(1), is(new File("file2")));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNegativeStart() throws Exception {
        new Range(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNegativeLength() throws Exception {
        new Range(0, -1);
    }
    
    @Test
    public void shouldBeInRange() throws Exception {
        Range range = new Range(3, 2);
        assertThat(range.in(3), is(true));
        assertThat(range.in(4), is(true));
    }

    @Test
    public void shouldNotBeInRange() throws Exception {
        Range range = new Range(3, 2);
        assertThat(range.in(2), is(false));
        assertThat(range.in(5), is(false));
    }

    @Test
    public void nothingShouldBeInRangeForZeroLength() throws Exception {
        Range range = new Range(3, 0);
        assertThat(range.in(2), is(false));
        assertThat(range.in(3), is(false));
        assertThat(range.in(4), is(false));
    }
}



