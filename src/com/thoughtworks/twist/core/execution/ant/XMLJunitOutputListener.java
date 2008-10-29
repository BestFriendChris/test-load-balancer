package com.thoughtworks.twist.core.execution.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;

import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;

import com.thoughtworks.twist.core.HTMLRenderer;
import com.thoughtworks.twist.core.Scenario;
import com.thoughtworks.twist.core.execution.ErrorInfo;
import com.thoughtworks.twist.core.execution.ScenarioExecutionException;
import com.thoughtworks.twist.core.execution.TestListener;
import com.thoughtworks.twist.core.parser.ScenarioParser;
import com.thoughtworks.twist.core.utils.FileUtils;

public class XMLJunitOutputListener implements TestListener {

    private final File reportsDir;
    private JUnitTestWrapper testSuite;
    private Test test;
    private long startTime;
    private TwistResultFormatter formatter;
    private final File[] toExecute;
    private int line;

    public XMLJunitOutputListener(File[] toExecute, File reportsDir) {
        this.toExecute = FileUtils.nullSafeCopy(toExecute);
        this.reportsDir = reportsDir;
        if (!reportsDir.exists() && !reportsDir.mkdirs()) {
            throw new ScenarioExecutionException("Unable to create directory to store execution results." + reportsDir);
        }
        formatter = new TwistResultFormatter();
    }

    public void error(String text, ErrorInfo info) {
        testSuite.setCounts(testSuite.runCount(), testSuite.failureCount(), testSuite.errorCount() + 1);
        moveScreenshot(info);
        logFailureAndErrors(line);
        formatter.addError(test, line, info);
    }

    private void moveScreenshot(ErrorInfo info) {
        if (info.getScreenshot() != null) {
            Move move = new Move() {

                public void log(String msg, int msgLevel) {
                }
            };
            move.setFile(info.getScreenshot());
            move.setTofile(new File(reportsDir.getAbsolutePath() + File.separator + "screenshots" + File.separator + testSuite.getName() + ".png"));
            move.execute();

            move.setFile(info.getThumbnailFile());
            move.setTofile(new File(reportsDir.getAbsolutePath() + File.separator + "screenshots" + File.separator + testSuite.getName() + "_thumb.png"));
            move.execute();
        }
    }

    public void fail(String text, ErrorInfo info) {
        testSuite.setCounts(testSuite.runCount(), testSuite.failureCount() + 1, testSuite.errorCount());
        moveScreenshot(info);
        logFailureAndErrors(line);
        formatter.addFailure(test, line, info);
    }

    public void scenarioEnd(String testName) {
        testSuite.setRunTime(System.currentTimeMillis() - startTime);
        testSuite.setCounts(testSuite.runCount() + 1, testSuite.failureCount(), testSuite.errorCount());
        formatter.endTest(test);
        formatter.endTestSuite(testSuite);
    }

    public void scenarioStart(String testName) {
        startTime = System.currentTimeMillis();
        formatter = formatter(testName);
        testName = new File(testName).getName();
        testSuite = new JUnitTestWrapper(testName);
        test = test(testName);
        formatter.startTest(test);
        formatter.startTestSuite(testSuite);
    }

    private TwistResultFormatter formatter(String testName) {
        try {
            TwistResultFormatter formatter = new TwistResultFormatter();
            formatter.setScenarioAsHtml(getHtmlStringForScenario(testName));
            formatter.setOutput(new FileOutputStream(new File(reportsDir + File.separator + "TWIST_TEST-" + "-scenarios."
                                                              + new File(testName).getName().replaceAll("\\.scn$", "") + ".xml")));
            return formatter;
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    private String getHtmlStringForScenario(String testName) {
        for (File file : toExecute) {
            if (file.getAbsolutePath().endsWith(testName)) {
                HTMLRenderer renderer = new HTMLRenderer();
                Scenario scenario = new ScenarioParser().parse(file);
                scenario.process(renderer);
                return renderer.html();
            }
        }
        throw new RuntimeException("Could not find a scenario with the name:" + testName);
    }

    public void executionEnd() {
        // do nothing
    }

    public void executionStart(int numScenarios) {
        // do nothing
    }

    public void lineEnd(int line, String lineContents) {
        // do nothing
    }

    public void lineStart(int line, String lineContents) {
        this.line = line;
        // do nothing
    }

    private Test test(final String lineContents) {
        return new TestWrapper(lineContents);
    }

    private void logFailureAndErrors(int line) {
        formatter.setScenarioAsHtml(new ErrorReportingModel().renderErrors(formatter.getScenarioAsHtml(), testSuite.getName(), line));
    }

    private static final class JUnitTestWrapper extends JUnitTest {

        private final String name;

        private JUnitTestWrapper(String name) {
            super(name);
            this.name = name;
        }

        public int hashCode() {
            return name.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            JUnitTestWrapper other = (JUnitTestWrapper) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            return true;
        }

    }

    static final class TestWrapper extends TestCase {

        final String lineContents;

        private TestWrapper(String lineContents) {
            this.lineContents = lineContents;
        }

        public int countTestCases() {
            return 0;
        }

        public void run(TestResult arg0) {
        }

        public String getName() {
            return lineContents;
        }

        public int hashCode() {
            return lineContents.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            TestWrapper other = (TestWrapper) obj;
            if (lineContents == null) {
                if (other.lineContents != null) {
                    return false;
                }
            } else if (!lineContents.equals(other.lineContents)) {
                return false;
            }
            return true;
        }
    }

    public void failCell(int tableIndex, int rowIndex, int columnIndex, String message) {
    }

    public void passCell(int tableIndex, int rowIndex, int columnIndex) {
    }
}
