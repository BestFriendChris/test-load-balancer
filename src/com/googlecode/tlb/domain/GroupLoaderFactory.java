package com.googlecode.tlb.domain;

import com.googlecode.tlb.domain.GroupLoader;
import com.googlecode.tlb.support.cruise.EnvBasedGroupLoader;
import com.googlecode.tlb.support.cruise.AgentBasedGroupLoader;
import com.googlecode.tlb.support.cruise.CruiseAgentSession;
import com.googlecode.tlb.support.cruise.CurrentJob;

import java.util.Map;

public class GroupLoaderFactory {

    public static GroupLoader getInstance() {
        return getInstance(System.getenv());
    }

    public static GroupLoader getInstance(Map<String, String> envs) {
        if (AgentBasedGroupLoader.satisfy(envs)) {
            return new AgentBasedGroupLoader(new CruiseAgentSession(), new CurrentJob());
        } else if (EnvBasedGroupLoader.satisfy(envs)) {
            return new EnvBasedGroupLoader();
        } else {
            return new LocalGroupLoader();
        }
    }


}
