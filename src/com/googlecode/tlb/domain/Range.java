package com.googlecode.tlb.domain;

import java.util.Arrays;
import java.util.List;
import static java.lang.System.arraycopy;

public class Range {
    private final int start;
    private final int length;

    public Range(int start, int length) {
        if (start < 0) {
            throw new IllegalArgumentException("Negative start: " + start);
        }
        if (length < 0) {
            throw new IllegalArgumentException("Negative length: " + length);
        }
        this.start = start;
        this.length = length;
    }

    public String toString() {
        return "Start from [" + this.start + "] with length as [" + this.length + "]";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        if (length != range.length) return false;
        if (start != range.start) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = start;
        result = 31 * result + length;
        return result;
    }

    public List<Object> in(List testFiles) {
        final Object[] files = new Object[this.length];
        arraycopy(testFiles.toArray(), start, files, 0, this.length);
        return Arrays.asList(files);
    }

    public boolean in(int index) {
        return index >= start && index < start + length;
    }
}
