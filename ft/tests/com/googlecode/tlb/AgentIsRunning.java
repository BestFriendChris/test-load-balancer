package com.googlecode.tlb;

import com.googlecode.tlb.utils.SystemUtil;
import com.googlecode.tlb.utils.FileUtil;
import static com.googlecode.tlb.utils.FileUtil.deleteFolder;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

public class AgentIsRunning extends ProcessIsRunning {

    public static final String AGENT_ROOT = "ft/integration/cruise-agent-1.1";
    public static final String AGENT_PIPELINES = AGENT_ROOT + "/pipelines";

    protected boolean isProcessStopped() {
        if (SystemUtil.isWindows()) {
            return isProcessStoppedOnWindows("cruise agent - agent.cmd");
        } else {
            return true;
        }
    }

    @Override
    public void start() throws Exception {
        File pipelines = new File(AGENT_PIPELINES);
        if (pipelines.exists()) {
            deleteFolder(pipelines);
            pipelines.mkdir();
        }
        super.start();    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected String startCommand() {
        return SystemUtil.isWindows() ? "start-agent.bat" : "./agent.sh";
    }

    protected String stopCommand() {
        return SystemUtil.isWindows() ? "stop-agent.bat" : "./stop-agent.sh";
    }

    protected String getWorkingDir() {
        return this.AGENT_ROOT;
    }

    protected Map<String, String> getStartEnvVariables() {
        Map<String, String> env = new HashMap<String, String>();
        env.put("CRUISE_SERVER", "127.0.0.1");
        env.put("VNC", "N");
        env.put("STOP_BEFORE_STARTUP", "N"); //else when test agent start it will kill "real" agent
        env.put("PRODUCTION_MODE", "N");
        return env;
    }
}
