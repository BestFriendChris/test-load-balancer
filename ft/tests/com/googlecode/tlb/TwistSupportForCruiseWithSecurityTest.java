package com.googlecode.tlb;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class TwistSupportForCruiseWithSecurityTest extends SupportForCruiseWithSecurityTestBase {
    @Before
    public void setUp() throws Exception {
        File hgRepo = createHgRepo("twist", "ft/twist/tlb-twist");
        setUp(hgRepo);
    }

    @Test
    public void shouldRunUnderCruise() throws Exception {
        Thread.sleep(90000);
        assertJobTestCount("job-1", 2);
        assertJobTestCount("job-2", 1);
    }
}
