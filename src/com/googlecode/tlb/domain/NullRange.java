package com.googlecode.tlb.domain;

import com.googlecode.tlb.domain.Range;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class NullRange extends Range {

    public NullRange() {
        super(0, 0);
    }

    public List<File> in(File[] testFiles) {
        return new ArrayList<File>();
    }
}
