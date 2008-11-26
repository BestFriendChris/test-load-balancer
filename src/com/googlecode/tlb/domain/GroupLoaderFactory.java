package com.googlecode.tlb.domain;

import com.googlecode.tlb.domain.GroupLoader;
import com.googlecode.tlb.support.cruise.LocalGroupLoader;
import com.googlecode.tlb.support.cruise.AgentBasedGroupLoader;
import com.googlecode.tlb.support.cruise.CruiseAgentSession;

public class GroupLoaderFactory {

    public static GroupLoader getInstance() {
        if (System.getenv(LocalGroupLoader.INDEX) == null || System.getenv(LocalGroupLoader.PIECES) == null) {
            return new AgentBasedGroupLoader(new CruiseAgentSession(), new CurrentJob());
        } else {
            return new LocalGroupLoader();
        }
    }
}
