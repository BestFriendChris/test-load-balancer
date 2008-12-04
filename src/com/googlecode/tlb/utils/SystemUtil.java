package com.googlecode.tlb.utils;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class SystemUtil {
    private static final Logger LOG = Logger.getLogger(SystemUtil.class);

    public static int runCommand(File directory, String... cmd) throws IOException, InterruptedException {
        return runCommand(new HashMap<String, String>(), directory, cmd);
    }

    public static int runCommand(Map<String, String> env, File directory, String... cmd) throws IOException, InterruptedException {
        Process p = launchCommand(env, directory, cmd);
        p.waitFor();
        return p.exitValue();
    }

    public static Process launchCommand(File directory, String... cmd) throws IOException {
        return launchCommand(new HashMap<String, String>(), directory, cmd);
    }

    public static Process launchCommand(Map<String, String> env, File directory, String... cmd) throws IOException {
        System.out.println("Running " + Arrays.asList(cmd) + " under " + directory.getAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.environment().putAll(env);
        pb.directory(directory);
        Process p = pb.start();
        logProcessOutput(p.getInputStream());
        return p;
    }

    public static void logProcessOutput(InputStream processStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(processStream));
        String line;
        while ((line = reader.readLine()) != null) {
            LOG.info(line);
        }
        IOUtils.closeQuietly(processStream);
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public static String antCommand() {
        return isWindows() ? "ant.bat" : "ant";
    }
}
