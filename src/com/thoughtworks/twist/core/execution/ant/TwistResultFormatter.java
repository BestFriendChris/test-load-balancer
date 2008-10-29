package com.thoughtworks.twist.core.execution.ant;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.AssertionFailedError;
import junit.framework.Test;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitResultFormatter;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitVersionHelper;
import org.apache.tools.ant.taskdefs.optional.junit.XMLConstants;
import org.apache.tools.ant.taskdefs.optional.junit.XMLJUnitResultFormatter;
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.DateUtils;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.thoughtworks.twist.core.execution.ErrorInfo;

class TwistResultFormatter extends XMLJUnitResultFormatter implements JUnitResultFormatter, XMLConstants {

    private static final double NUMBER_OF_MILLIS_IN_1_SEC = 1000.0;
    private static final String UNKNOWN = "unknown";
    private String scenarioAsHtml;

    private DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    public void setScenarioAsHtml(String scenarioAsHtml) {
        this.scenarioAsHtml = scenarioAsHtml;
    }

    private Document doc;
    private Element rootElement;
    private Hashtable<Test, Element> testElements = new Hashtable<Test, Element>();
    private Hashtable<Test, Test> failedTests = new Hashtable<Test, Test>();
    private Hashtable<Test, Long> testStarts = new Hashtable<Test, Long>();
    private OutputStream out;
    private Date suiteStartTime;
    private Test previousProcessedFailure;

    public TwistResultFormatter() {
    }

    public void setOutput(OutputStream out) {
        this.out = out;
    }

    public void setSystemOutput(String out) {
        formatOutput(SYSTEM_OUT, out);
    }

    public void setSystemError(String out) {
        formatOutput(SYSTEM_ERR, out);
    }

    public void startTestSuite(JUnitTest suite) {
        doc = getDocumentBuilder().newDocument();
        rootElement = doc.createElement(TESTSUITE);
        String n = suite.getName();
        rootElement.setAttribute(ATTR_NAME, n == null ? UNKNOWN : n);

        suiteStartTime = new Date();
        final String timestamp = DateUtils.format(suiteStartTime, DateUtils.ISO8601_DATETIME_PATTERN);
        rootElement.setAttribute(TIMESTAMP, timestamp);
        rootElement.setAttribute(HOSTNAME, getHostname());

        Element propsElement = doc.createElement(PROPERTIES);
        rootElement.appendChild(propsElement);
        Properties props = suite.getProperties();
        if (props != null) {
            Enumeration<?> e = props.propertyNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                Element propElement = doc.createElement(PROPERTY);
                propElement.setAttribute(ATTR_NAME, name);
                propElement.setAttribute(ATTR_VALUE, props.getProperty(name));
                propsElement.appendChild(propElement);
            }
        }
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    public void endTestSuite(JUnitTest suite) {
        suite.setRunTime(System.currentTimeMillis() - suiteStartTime.getTime());
        rootElement.setAttribute(ATTR_TESTS, "" + suite.runCount());
        rootElement.setAttribute(ATTR_FAILURES, "" + suite.failureCount());
        rootElement.setAttribute(ATTR_ERRORS, "" + suite.errorCount());
        rootElement.setAttribute(ATTR_TIME, "" + (suite.getRunTime() / NUMBER_OF_MILLIS_IN_1_SEC));
        if (out != null) {
            Writer wri = null;
            try {
                wri = new BufferedWriter(new OutputStreamWriter(out, "UTF8"));
                wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                (new DOMElementWriter()).write(rootElement, wri, 0, "  ");
                wri.flush();
            } catch (IOException exc) {
                exc.printStackTrace();
                throw new BuildException("Unable to write log file", exc);
            }
            finally {
                if ((out != System.out) && (out != System.err)) {
                    FileUtils.close(wri);
                }
            }
        }
    }

    public void startTest(Test t) {
        testStarts.put(t, new Long(System.currentTimeMillis()));
    }

    public void endTest(Test test) {
        Element currentTest = null;
        if (!failedTests.containsKey(test)) {
            currentTest = doc.createElement(TESTCASE);
            String n = JUnitVersionHelper.getTestCaseName(test);
            currentTest.setAttribute(ATTR_NAME, n == null ? UNKNOWN : n);
            currentTest.setAttribute(ATTR_CLASSNAME, getTestCaseClassName(test));
            rootElement.appendChild(currentTest);
            testElements.put(test, currentTest);
        } else {
            currentTest = testElements.get(test);
        }

        Long l = testStarts.get(test);
        currentTest.setAttribute(ATTR_TIME, "" + ((System.currentTimeMillis() - l.longValue()) / NUMBER_OF_MILLIS_IN_1_SEC));
    }

    public void addFailure(Test test, Throwable t) {
        throw new UnsupportedOperationException("Should not have been invoked");
        // formatError(FAILURE, test, t);
    }

    public void addFailure(Test test, AssertionFailedError t) {
        throw new UnsupportedOperationException("Should not have been invoked");
        // addFailure(test, (Throwable) t);
    }

    public void addError(Test test, Throwable t) {
        throw new UnsupportedOperationException("Should not have been invoked");
        // formatError(ERROR, test, t);
    }

    public void addFailure(Test test, int lineNumber, ErrorInfo info) {
        formatError(FAILURE, test, lineNumber, info);
    }

    public void addError(Test test, int lineNumber, ErrorInfo info) {
        formatError(ERROR, test, lineNumber, info);
    }

    private void formatError(String type, Test test, int lineNumber, ErrorInfo info) {
        if (test != null) {
            endTest(test);
            failedTests.put(test, test);
        }

        Element nested = doc.createElement(type);
        Element currentTest = null;
        if (test != null) {
            currentTest = testElements.get(test);
        } else {
            currentTest = rootElement;
        }
        currentTest.appendChild(nested);

        String message = info.message();
        if (message == null) {
            message = "";
        }
        message = message.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("(\r\n|\r|\n)", "<br/>");
        if ((message.length() > 0)) {
            nested.setAttribute(ATTR_MESSAGE, message);
        }

        nested.setAttribute(ATTR_TYPE, info.getClass().getName());

        String strace = prependScenarioInfo(test, lineNumber, info.getFilteredStackTrace());
        Text trace = doc.createTextNode(strace);
        // FIXME: Remove the stoopid previousTestFailure and fix this properly
        if ((scenarioAsHtml != null) && !test.equals(previousProcessedFailure)) {
            previousProcessedFailure = test;
            nested.appendChild(doc.createTextNode(scenarioAsHtml));
        }
        nested.appendChild(trace);
    }

    private String prependScenarioInfo(Test test, int lineNumber, String stackTrace) {
        return JUnitVersionHelper.getTestCaseName(test) + ":" + lineNumber + "\n" + stackTrace;
    }

    private void formatOutput(String type, String output) {
        Element nested = doc.createElement(type);
        rootElement.appendChild(nested);
        nested.appendChild(doc.createCDATASection(output));
    }

    private String getTestCaseClassName(Test test) {
        return test.getClass().getName();
    }

    public String getScenarioAsHtml() {
        return scenarioAsHtml;
    }

}
