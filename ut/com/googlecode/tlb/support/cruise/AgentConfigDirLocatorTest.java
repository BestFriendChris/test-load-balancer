package com.googlecode.tlb.support.cruise;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import com.googlecode.tlb.utils.FileUtil;

import java.io.File;
import java.io.IOException;

public class AgentConfigDirLocatorTest {
    private File configDir;
    private File baseFolder;

    @Before
    public void setUp() throws Exception {
        baseFolder = FileUtil.createTempFolder();
        configDir = new File(baseFolder, "config");
        configDir.mkdir();
        File agentJks = new File(configDir, AgentProxy.AGENT_JKS);
        agentJks.createNewFile();
        File trustJks = new File(configDir, AgentProxy.TRUST_JKS);
        trustJks.createNewFile();
        File childDir = new File(baseFolder, "child1");
        childDir.mkdir();
        File workingDir = new File(childDir, "child2");
        workingDir.mkdir();
        System.setProperty("user.dir", workingDir.getPath());
        assertThat(new File(".").getCanonicalPath(), is(workingDir.getCanonicalPath()));
    }

    @After
    public void tearDown() throws Exception {
        FileUtil.deleteFolder(baseFolder);
    }

    @Test
    public void shouldReturnDirThatContainsCertificateFiles() throws IOException {
        AgentConfigDirLocator locator = new AgentConfigDirLocator();
        assertThat(locator.agentConfigDir().getCanonicalFile(), is(configDir.getCanonicalFile()));
    }
}
