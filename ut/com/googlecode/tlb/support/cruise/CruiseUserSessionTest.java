package com.googlecode.tlb.support.cruise;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Ignore;
import static org.junit.Assert.assertThat;
import static org.hamcrest.text.StringContains.containsString;

public class CruiseUserSessionTest {

    @Before
    public void setUp() {
        System.setProperty("cruise.server.url", "http://10.18.7.51:8153/cruise/");
    }

    @After
    public void tearDown() {
        System.clearProperty("cruise.server.url");
    }

    @Test
    @Ignore
    public void shouldGetPipelineStatusJson() {
        CruiseUserSession connector = new CruiseUserSession("jez", "badger");
        String content = connector.pipelineStatus("cruise", "dev", "linux-firefox");
        assertThat(content, containsString("linux-firefox-2"));
    }
}
