package com.thoughtworks.twist.core.execution.ant;

import java.io.File;

import com.thoughtworks.twist.core.execution.ConsoleOutputListener;
import com.thoughtworks.twist.core.execution.DefaultTestListener;
import com.thoughtworks.twist.core.execution.ErrorInfo;
import com.thoughtworks.twist.core.execution.TwistLauncher;
import com.thoughtworks.twist.messaging.event.PlaybackAcknowledgement;
import com.thoughtworks.twist.messaging.service.editor.DefaultEditorService;
import com.thoughtworks.twist.messaging.service.remote.DefaultRemoteProcessService;

/**
 * The ant task to execute a scenario
 */
public class ScenarioExecutorAntMain {
    
    public static final int PASS_EXIT_CODE = 0;
    public static final int FAILURE_EXIT_CODE = 1;
    public static final int ERROR_EXIT_CODE = 2;
    public static final int FAILURE_AND_ERROR_EXIT_CODE = 3;
    public static final int INVALID_INPUT_EXIT_CODE = 4;
    
    
    private final File reportsDir;
    private final File[] toExecute;
    private ExitCodeListener listener;

    public ScenarioExecutorAntMain(File reportsDir, File scenarioDir, final String tags) {
        this.toExecute = scenarioDir.getAbsoluteFile().listFiles(new TagFileFilter(tags));
        System.out.println("The following scenarios from the directory '" + scenarioDir + "' matched the filter: '" + tags + "'.");
        for (File file : toExecute) {
            System.out.println(" - " + file);
        }
        this.reportsDir = reportsDir;
    }

    public ExitCodeListener start() {
        DefaultEditorService editorService = new DefaultEditorService();
        editorService.addTestListener(new ConsoleOutputListener());
        editorService.addTestListener(new XMLJunitOutputListener(toExecute, reportsDir));

        listener = new ExitCodeListener();
        editorService.addTestListener(listener);
        editorService.start(new Launcher(), new PlaybackAcknowledgement(toExecute));
        return waitForExecutionEnd(this);
    }

    // args:
    // scenariosDir
    // reportsDir
    public static void main(String[] args) {
        File scenarioDir = new File(args[0]);
        File reportsDir = new File(args[1]).getAbsoluteFile();
        String tags = "";
        if (args.length >= 2) {
            tags = args[2];
        }
        ScenarioExecutorAntMain executor = new ScenarioExecutorAntMain(reportsDir, scenarioDir, tags);
        ExitCodeListener listener = executor.start();

        System.exit(exitCode(listener));
    }

    public static int exitCode(ExitCodeListener listener) {
        boolean error = listener.error;
        boolean failure = listener.failure;
        if (error && failure) {
            return FAILURE_AND_ERROR_EXIT_CODE;
        }
        if (error) {
            return ERROR_EXIT_CODE;
        }
        if (failure) {
            return FAILURE_EXIT_CODE;
        }
        return PASS_EXIT_CODE;

    }

    private ExitCodeListener waitForExecutionEnd(ScenarioExecutorAntMain executor) {
        while (!executor.listener.executionEnd) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return executor.listener;
    }

    private static final class Launcher implements TwistLauncher {

        public void launch(int port) {
            DefaultRemoteProcessService remoteProcessService = new DefaultRemoteProcessService(port);
            remoteProcessService.start();
        }
    }

    public static class ExitCodeListener extends DefaultTestListener {

        private boolean error;
        private boolean failure;
        private boolean executionEnd;

        public void error(String text, ErrorInfo info) {
            this.error = true;
        }

        public void fail(String text, ErrorInfo info) {
            this.failure = true;
        }

        public void executionEnd() {
            this.executionEnd = true;
        }
    }
}
