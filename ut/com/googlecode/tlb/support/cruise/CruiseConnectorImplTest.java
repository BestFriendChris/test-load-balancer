package com.googlecode.tlb.support.cruise;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertThat;
import static org.hamcrest.text.StringContains.containsString;

public class CruiseConnectorImplTest {

    @Before
    public void setUp() {
        System.setProperty("cruise.server.url", "http://10.18.7.51:8153/cruise/");
    }

    @After
    public void tearDown() {
        System.clearProperty("cruise.server.url");
    }

    @Test
    public void shouldGetPipelineStatusJson() {
        CruiseConnectorImpl connector = new CruiseConnectorImpl("jez", "badger");
        String content = connector.pipelineStatus("cruise", "dev", "linux-firefox");
        assertThat(content, containsString("linux-firefox-2"));
    }
}
