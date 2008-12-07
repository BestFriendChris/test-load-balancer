package com.googlecode.tlb.helpers;

import com.googlecode.tlb.utils.SystemUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public abstract class ProcessIsRunning {
    public void destroy() throws Exception {
        stop();
    }

    public void afterPropertiesSet() throws Exception {
        start();
    }


    public void start() throws Exception {
        execute(startCommand(), getStartEnvVariables());
    }

    public void stop() throws Exception {
        String command = stopCommand();
        do {
            System.out.println("Stoping process...                 " + command);
            execute(command, new HashMap<String, String>());
            Thread.sleep(1000);
        }
        while (!isProcessStopped());
    }

    private void execute(String command, Map<String, String> envVariables) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        builder.environment().put("PID_FILE", new File(getWorkingDir(), "process.pid").getCanonicalPath());
        builder.environment().put("DAEMON", "Y");
        builder.environment().put("MANUAL_SETTING", "Y");
        builder.environment().putAll(envVariables);
        builder.directory(new File(getWorkingDir()));

        if (SystemUtil.isWindows()) {
            builder.command("cmd ", "/c", command);
        } else {
            builder.command(command);
        }

        Process process = builder.start();
        NewStreamPumper.pump(process.getInputStream());
        process.getOutputStream().close();
    }

    protected abstract boolean isProcessStopped();

    protected abstract String startCommand();

    protected abstract String stopCommand();

    protected abstract String getWorkingDir();

    protected abstract Map<String, String> getStartEnvVariables();

    protected boolean isProcessStoppedOnWindows(String windowTitle) {
        return true;
    }

    public static class NewStreamPumper {
        public static void pump(final InputStream inputStream) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        BufferedReader sysout = new BufferedReader(new InputStreamReader(inputStream));
                        String line = null;
                        while ((line = sysout.readLine()) != null) {
                            System.out.println(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        }
    }
}
