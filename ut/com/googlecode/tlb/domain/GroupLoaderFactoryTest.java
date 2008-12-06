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

public class GroupLoaderFactoryTest {
    @Test
    @Ignore
    public void shouldCreateEnvBasedLoaderWhenPiceseAndCurrentIndexIsSpecified() {
        System.setProperty("splittedPieces", "5");
        System.setProperty("pieceIndex", "1");
        final GroupLoader groupLoader = GroupLoaderFactory.getInstance();
        assertThat(groupLoader, is(instanceOf(EnvBasedGroupLoader.class)));
    }


    @Test
    public void shouldCreateAgentBasedCruiseLoaderWhenPiceseAndCurrentIndexIsNotSpecified() {
        final GroupLoader groupLoader = GroupLoaderFactory.getInstance();
        assertThat(groupLoader, is(instanceOf(AgentBasedGroupLoader.class)));
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        System.clearProperty("splittedPieces");
        System.clearProperty("pieceIndex");
    }

}



