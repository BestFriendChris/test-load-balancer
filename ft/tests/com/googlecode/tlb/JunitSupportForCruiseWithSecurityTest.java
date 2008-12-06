package com.googlecode.tlb;

import static com.googlecode.tlb.utils.ExceptionUtils.bomb;
import static com.googlecode.tlb.utils.ExceptionUtils.bombIf;
import com.googlecode.tlb.utils.FileUtil;
import static com.googlecode.tlb.utils.FileUtil.copyFile;
import static com.googlecode.tlb.utils.FileUtil.*;
import com.googlecode.tlb.utils.SystemUtil;
import static com.googlecode.tlb.utils.SystemUtil.launchCommand;
import static com.googlecode.tlb.utils.SystemUtil.runCommand;
import static org.apache.commons.io.FileUtils.copyDirectory;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class JunitSupportForCruiseWithSecurityTest {

    @Test
    public void shouldRunUnderCruise() throws Exception {
        File serverDir = new File("C:\\hg-cruise\\cruise\\target\\cruise-server-1.1");
        File agentDir = new File("C:\\hg-cruise\\cruise\\target\\cruise-agent-1.1");
        if (serverDir.exists() && agentDir.exists()) {
            String pipelineName = UUID.randomUUID().toString();
            File hgRepo = createHgRepo(pipelineName);
            File configFile = new File(serverDir, "cruise-config.xml");
            copyFile(new File("ft/junit/config", "cruise-config.xml"), configFile);
            replace(configFile, "${URL}", hgRepo.getAbsolutePath());
            replace(configFile, "connectfour", pipelineName);
            copyFile(new File("ft/junit/config", "password.properties"), new File(serverDir, "password.properties"));
            launchCommand(serverDir, serverCommand(serverDir));
            waitForServerStarted();
            launchCommand(agentDir, agentCommand(agentDir));
            runCommand("firefox", "http://localhost:8153");
        }
    }

    private void waitForServerStarted() throws InterruptedException {
        int timeout = 20;
        int elapsed = 0;
        while (elapsed < timeout) {
            if (SystemUtil.isLocalPortOccupied(8153)) {
                return;
            }
            Thread.sleep(1000);
            elapsed += 1;
        }
        if (!SystemUtil.isLocalPortOccupied(8153)) {
            bomb("Server not started after " + timeout + " seconds!");
        }

    }

    private static File createHgRepo(String pipelineName) throws IOException, InterruptedException {
        File tempRepo = new File("target/" + pipelineName);
        deleteDirectory(tempRepo);
        copyDirectory(new File("ft/junit/connectfour"), tempRepo);
        bombIf(runCommand(tempRepo, "hg", "init") != 0, "hg init failed!");
        bombIf(runCommand(tempRepo, "hg", "addremove") != 0, "hg addremove failed!");
        bombIf(runCommand(tempRepo, "hg", "ci", "-m", "Initial checkin") != 0, "hg ci failed");
        return tempRepo;
    }

    private String agentCommand(File agentDir) {
        return SystemUtil.isWindows()
                ? new File(agentDir, "start-agent.bat").getAbsolutePath()
                : "agent.sh";
    }

    private String serverCommand(File serverDir) {
        return SystemUtil.isWindows()
                ? new File(serverDir, "start-server.bat").getAbsolutePath()
                : "server.sh";
    }

}
