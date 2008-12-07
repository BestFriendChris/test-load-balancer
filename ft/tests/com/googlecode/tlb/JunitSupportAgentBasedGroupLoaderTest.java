package com.googlecode.tlb;

import org.junit.Test;
import org.junit.Before;

import java.io.File;

import com.googlecode.tlb.helpers.SupportForCruiseWithSecurityTestBase;


public class JunitSupportAgentBasedGroupLoaderTest extends SupportForCruiseWithSecurityTestBase {
    @Before
    public void setUp() throws Exception {
        File hgRepo = createHgRepo("connectfour", "ft/junit/connectfour");
        super.setUp(hgRepo);
    }

    @Test
    public void shouldRunUnderCruise() throws Exception {
        Thread.sleep(90000);
        assertJobTestCount("job-1", 11);
        assertJobTestCount("job-2", 10);
    }
}
