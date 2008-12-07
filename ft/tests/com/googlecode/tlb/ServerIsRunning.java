package com.googlecode.tlb;

import com.googlecode.tlb.utils.SystemUtil;
import com.googlecode.tlb.utils.FileUtil;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;

public class ServerIsRunning extends ProcessIsRunning {
    public static final String SERVER_ROOT = "ft/integration/cruise-server-1.1";
    private String repo;
    private String pipeline;

    public ServerIsRunning(String repo, String pipeline) {
        this.repo = repo;
        this.pipeline = pipeline;
    }

    public void start() throws Exception {
        prepare();
        super.start();
        waitForServerToStart();
    }

    protected boolean isProcessStopped() {
        if (SystemUtil.isWindows()) {
            return isProcessStoppedOnWindows("cruise server - server.cmd");
        } else {
            return true;
        }
    }

    private void waitForServerToStart() throws Exception {
        Thread.sleep(20000);
    }


    protected String getWorkingDir() {
        return SERVER_ROOT;
    }

    protected Map<String, String> getStartEnvVariables() {
        HashMap<String, String> env = new HashMap<String, String>();
        env.put("CRUISE_SERVER_SYSTEM_PROPERTIES",
                "-Dalways.reload.config.file=true " +
                        "-Dcruise.buildCause.producer.interval=10000 " +
                        "-Dcruise.pipelineStatus.cache.interval=800");
        return env;
    }


    protected String startCommand() {
        return SystemUtil.isWindows() ? "start-server.bat" : "./server.sh";
    }

    protected String stopCommand() {
        return SystemUtil.isWindows() ? "stop-server.bat" : "./stop-server.sh";
    }

    public void prepare() throws IOException {
        HashMap hashMap = new HashMap();
        hashMap.put("${URL}", repo);
        hashMap.put("${PIPELINE}", pipeline);
        File serverDir = new File(SERVER_ROOT);
        FileUtil.copyFileWithReplacement(new File("ft/junit/config", "cruise-config.xml"),
                new File(serverDir, "cruise-config.xml"),
                hashMap);
        FileUtil.copySingleFile(new File("ft/junit/config", "password.properties"), new File(serverDir, "password.properties"));
        
    }
}
