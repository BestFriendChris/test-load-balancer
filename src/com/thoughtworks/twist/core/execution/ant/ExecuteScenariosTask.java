package com.thoughtworks.twist.core.execution.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.apache.tools.ant.types.Environment.Variable;

/**
 * Ant task to execute scenarios.
 * Accepts the following attributes
 * <ul>
 *  <li>scenarioDir: directory where scenarios are stored
 *  <li>reportsDir: directory to store reports
 *  <li>tags: the tags to be used to filter scenarios
 *  <li>errorProperty: Property to be set if there is an error.
 *  <li>failureProperty: Property to be set if there is a failure
 *  <li>haltonfailure: Build reports failure if one or more scenarios had test failure(s)
 *  <li>haltonerror: Build fails if one or more scenarios had test execution errors
 *  <li>resultProperty: A property that contains the exit code of the execution 
 *  <li>classpath: Similar semantics as in Java task
 *  <li>classpathref: Similar semantics as in Java task
 *  <li>jvmArgs: Similar semantics as in Java task
 *  <li>maxMemory: Similar semantics as in Java task
 *  <li>jvmVersion: Similar semantics as in Java task. (Only 1.5 and 1.6 are supported)
 *  <li>timeout:Similar semantics as in Java task
 *  </ul>
 */
public class ExecuteScenariosTask extends Task {

    private static final String exitCodePropertyFromJVM = "twist.error.property"; 

    private File scenarioDir;
    private File reportsDir;
    private Java java;
    private String tags = "";
    private String errorproperty;
    private String failureproperty;
    private boolean haltonfailure = false;
    private boolean haltonerror = false;
    
    // This would allow us to return the exec status of ScenarioExecutorAntMainTask as the 'resultProperty' of this task.
    private String resultproperty = exitCodePropertyFromJVM;
    
    public ExecuteScenariosTask() {
        this(new Java());
    }

    ExecuteScenariosTask(Java java) {
        this.java = java;
    }

    public void setReportsDir(File reportsDir) {
        this.reportsDir = reportsDir;
    }

    public void setScenarioDir(File scenarioDir) {
        this.scenarioDir = scenarioDir;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @param errorproperty
     *            The name of a property to set in the event of an error.
     */
    public void setErrorproperty(String errorproperty) {
        this.errorproperty = errorproperty;
    }

    /**
     * @param failureproperty
     *            The name of a property to set in the event of a failure (errors are considered failures as well).
     */
    public void setFailureproperty(String failureproperty) {
        this.failureproperty = failureproperty;
    }

    /**
     * Stop the build process if an error occurs during the test run. Default is off
     */
    public void setHaltonerror(boolean haltonerror) {
        this.haltonerror = haltonerror;
    }

    /**
     * Stop the build process if a test fails (errors are considered failures as well). Default is off.
     */
    public void setHaltonfailure(boolean haltonfailure) {
        this.haltonfailure = haltonfailure;
    }

    public void addEnv(Variable var) {
        java.addEnv(var);
    }

    public void addSysproperty(Variable variable) {
        java.addSysproperty(variable);
    }

    public void addSyspropertyset(PropertySet sysp) {
        java.addSyspropertyset(sysp);
    }

    public void execute() {

        checkConfiguration();

        initializeJavaTask();

        java.execute();
        int errorCode = Integer.valueOf(getProject().getProperty(resultproperty));

        handleTestResult(errorCode);
        haltOnErrorOrFailureIfNecessary(errorCode);
    }

    protected String exitCodeFromJvmInternal() {
        return exitCodePropertyFromJVM;
    }

    public SysProperties getSysProperties() {
        return java.getSysProperties();
    }

    public void init() throws BuildException {
        super.init();

        java.setFork(true);
        java.setJVMVersion("1.5");
        java.setResultProperty(resultproperty);

        java.init();
    }

    public void maybeConfigure() throws BuildException {
        super.maybeConfigure();
        java.maybeConfigure();
    }

    public void reconfigure() {
        super.reconfigure();
        java.reconfigure();
    }

    public void setDescription(String desc) {
        super.setDescription(desc);
        java.setDescription(desc);
    }

    public void setOwningTarget(Target target) {
        super.setOwningTarget(target);
        java.setOwningTarget(target);
    }

    public void setProject(Project project) {
        super.setProject(project);
        java.setProject(project);
    }

    public void setLocation(Location location) {
        super.setLocation(location);
        java.setLocation(location);
    }

    public void setRuntimeConfigurableWrapper(RuntimeConfigurable wrapper) {
        super.setRuntimeConfigurableWrapper(wrapper);
        java.setRuntimeConfigurableWrapper(wrapper);
    }

    public void setTaskName(String name) {
        super.setTaskName(name);
        java.setTaskName(name);
    }

    public void setTaskType(String type) {
        super.setTaskType(type);
        java.setTaskType(type);
    }

    // //////////////////
    // the rest of these are delegated to the java task
    // //////////////////

    public void setClasspath(Path path) {
        java.setClasspath(path);
    }

    public void setClasspathRef(Reference classpathref) {
        java.setClasspathRef(classpathref);
    }

    public void setDir(File dir) {
        java.setDir(dir);
    }

    public void setJvmargs(String s) {
        java.setJvmargs(s);
    }

    /**
     * Supported versions are 1.5 and 1.6.
     * 
     * @param value
     *            the jvm version.
     */
    public void setJVMVersion(String value) {
        if (!value.equals("1.5") && !value.equals("1.6")) {
            throw new BuildException("Only supported JVM versions are: 1.5 and 1.6. You provided " + value);
        }

        java.setJVMVersion(value);
    }

    public void setMaxmemory(String max) {
        java.setMaxmemory(max);
    }

    public void setOutputproperty(String outputProp) {
        java.setOutputproperty(outputProp);
    }

    public void setResultProperty(String resultProperty) {
        this.resultproperty = resultProperty;
        java.setResultProperty(resultProperty);
    }

    public void setTimeout(Long value) {
        java.setTimeout(value);
    }

    private void haltOnErrorOrFailureIfNecessary(int errorCode) {
        switch (errorCode) {
            case ScenarioExecutorAntMain.ERROR_EXIT_CODE:
                if (haltonerror) {
                    throw new BuildException("Error(s) occured executing test(s) in '" + scenarioDir.getAbsolutePath() + "'", getLocation());
                }
                break;
            case ScenarioExecutorAntMain.FAILURE_AND_ERROR_EXIT_CODE:
                if (haltonerror || haltonfailure) {
                    throw new BuildException("Error(s)/Failure(s) occured executing test(s) in '" + scenarioDir.getAbsolutePath() + "'", getLocation());
                }
                break;
            case ScenarioExecutorAntMain.FAILURE_EXIT_CODE:
                if (haltonfailure) {
                    throw new BuildException("Test(s) failed in '" + scenarioDir.getAbsolutePath() + "'", getLocation());
                }
                break;
        }
    }

    protected void handleTestResult(int errorCode) {
        switch (errorCode) {
            // errors are failures as well.
            case ScenarioExecutorAntMain.ERROR_EXIT_CODE:
            case ScenarioExecutorAntMain.FAILURE_AND_ERROR_EXIT_CODE:
                setErrorProperty();
                setFailureProperty();
                break;
            case ScenarioExecutorAntMain.FAILURE_EXIT_CODE:
                setFailureProperty();
                break;
        }
    }

    private void checkConfiguration() {
        if (!scenarioDir.isDirectory()) {
            throw new BuildException("Scenario Directory '" + scenarioDir.getAbsolutePath() + "' does not exist or is not a directory", getLocation());
        }

        if (!reportsDir.isDirectory()) {
            throw new BuildException("Reports Directory '" + reportsDir.getAbsolutePath() + "' does not exist or is not a directory", getLocation());
        }
    }

    private void initializeJavaTask() {
        java.setClassname(ScenarioExecutorAntMain.class.getName());
        java.createArg().setValue(scenarioDir.getAbsolutePath());
        java.createArg().setValue(reportsDir.getAbsolutePath());
        java.createArg().setValue(tags);
        
        // Noticed that Task#init is not always called. Hence, setting this explicitly here.
        java.setResultProperty(resultproperty);
    }

    private void setErrorProperty() {
        if (errorproperty != null) {
            getProject().setNewProperty(errorproperty, "");
        }
    }

    private void setFailureProperty() {
        if (failureproperty != null) {
            getProject().setNewProperty(failureproperty, "");
        }
    }
}
