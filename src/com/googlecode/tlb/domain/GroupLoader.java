package com.googlecode.tlb.domain;

import com.googlecode.tlb.exceptions.JobNotFoundException;
import com.googlecode.tlb.domain.Group;

public interface GroupLoader {
    Group load() throws JobNotFoundException;
}
