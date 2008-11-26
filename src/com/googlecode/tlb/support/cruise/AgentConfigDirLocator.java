package com.googlecode.tlb.support.cruise;

import static com.googlecode.tlb.support.cruise.AgentProxy.*;
import static com.googlecode.tlb.utils.ExceptionUtils.bomb;

import java.io.File;
import java.io.IOException;

public class AgentConfigDirLocator {

    public File agentConfigDir() {
        try {
            return agentConfigDir(new File(".").getCanonicalFile());
        } catch (IOException e) {
            throw bomb(e);
        }
    }

    private File agentConfigDir(File folder) {
        if (folder == null) {
            return null;
        }
        if (testFolder(folder)) {
            return new File(folder, "config");
        } else {
            return agentConfigDir(folder.getParentFile());
        }
    }

    private boolean testFolder(File folder) {
        File configDir = new File(folder, "config");
        return configDir.isDirectory()
                && new File(configDir, AGENT_JKS).isFile()
                && new File(configDir, TRUST_JKS).isFile();
    }
}
