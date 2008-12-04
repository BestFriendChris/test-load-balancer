package com.googlecode.tlb;

import static com.googlecode.tlb.JunitSupportFunctionalTest.runAntCommand;
import static com.googlecode.tlb.utils.FileUtil.copyFile;
import static com.googlecode.tlb.utils.FileUtil.copyFileWithReplacement;
import com.googlecode.tlb.utils.SystemUtil;
import static com.googlecode.tlb.utils.ExceptionUtils.bombIf;
import static com.googlecode.tlb.utils.SystemUtil.runCommand;
import static com.googlecode.tlb.utils.SystemUtil.launchCommand;
import static org.apache.commons.io.FileUtils.copyDirectory;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class JunitSupportForCruiseWithSecurityTest {

    @Test
    public void shouldRunUnderCruise() throws Exception {
        File serverDir = new File("C:\\hg-cruise\\cruise\\target\\cruise-server-1.1");
        File agentDir = new File("C:\\hg-cruise\\cruise\\target\\cruise-agent-1.1");
        if (serverDir.exists() && agentDir.exists()) {
            File hgRepo = createHgRepo();
            copyFileWithReplacement(new File("ft/junit/config", "cruise-config.xml"),
                    new File(serverDir, "cruise-config.xml"),
                    "${URL}",
                    hgRepo.getAbsolutePath());
            copyFile(new File(serverDir, "password.properties"), new File("ft/junit/config", "password.properties"));
            launchCommand(serverDir, serverCommand(serverDir));
            launchCommand(agentDir, agentCommand(agentDir));
        }
    }

    private static File createHgRepo() throws IOException, InterruptedException {
        File tempRepo = new File("target/connectfour");
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
