package com.googlecode.tlb.domain;

import com.googlecode.tlb.domain.GroupLoader;
import com.googlecode.tlb.support.cruise.EnvBasedGroupLoader;
import com.googlecode.tlb.support.cruise.AgentBasedGroupLoader;
import com.googlecode.tlb.support.cruise.CruiseAgentSession;
import com.googlecode.tlb.support.cruise.CurrentJob;

public class GroupLoaderFactory {

    public static GroupLoader getInstance() {
        if (System.getenv(EnvBasedGroupLoader.INDEX) == null || System.getenv(EnvBasedGroupLoader.PIECES) == null) {
            return new AgentBasedGroupLoader(new CruiseAgentSession(), new CurrentJob());
        } else {
            return new EnvBasedGroupLoader();
        }
    }
}
