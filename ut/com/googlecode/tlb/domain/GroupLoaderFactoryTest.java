package com.googlecode.tlb.domain;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.Ignore;
import com.googlecode.tlb.domain.GroupLoader;
import com.googlecode.tlb.support.cruise.EnvBasedGroupLoader;
import com.googlecode.tlb.support.cruise.AgentBasedGroupLoader;
import com.googlecode.tlb.support.cruise.CurrentJob;

import java.util.HashMap;

public class GroupLoaderFactoryTest {


    @Test
    public void shouldCreateAgentBasedGroupLoaderIfAllCruiseJobKeyExsit() {
        HashMap<String, String> env = new HashMap<String, String>();
        env.put(CurrentJob.JOB_NAME_KEY, "linux-firefox");
        env.put(CurrentJob.CRUISE_SERVER_URL, "https://localhost:8153/cruise/");
        final GroupLoader groupLoader = GroupLoaderFactory.getInstance(env);
        assertThat(groupLoader, is(instanceOf(AgentBasedGroupLoader.class)));
    }

    @Test
    public void shouldCreatedEnvBasedGroupLoaderWhenINDEXandPIECESExist() {
        HashMap<String, String> env = new HashMap<String, String>();
        env.put(EnvBasedGroupLoader.INDEX, "1");
        env.put(EnvBasedGroupLoader.PIECES, "5");

        final GroupLoader groupLoader = GroupLoaderFactory.getInstance(env);
        assertThat(groupLoader, is(instanceOf(EnvBasedGroupLoader.class)));
    }

    @Test
    public void shouldCreateLocalGroupLoaderWhenNoKeys() {
        final GroupLoader groupLoader = GroupLoaderFactory.getInstance(new HashMap());
        assertThat(groupLoader, is(instanceOf(LocalGroupLoader.class)));
    }

    @Test
    public void shouldConsiderAgentBasedGroupLoaderFirst() {
        HashMap<String, String> env = new HashMap<String, String>();
        env.put(CurrentJob.JOB_NAME_KEY, "linux-firefox");
        env.put(CurrentJob.CRUISE_SERVER_URL, "https://localhost:8153/cruise/");
        env.put(EnvBasedGroupLoader.INDEX, "1");
        env.put(EnvBasedGroupLoader.PIECES, "5");
        final GroupLoader groupLoader = GroupLoaderFactory.getInstance(env);
        assertThat(groupLoader, is(instanceOf(AgentBasedGroupLoader.class)));
    }

    @Before
    public void setUp() {
    }


}



