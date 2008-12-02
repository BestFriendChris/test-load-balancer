package com.googlecode.tlb.support.twist;

import com.thoughtworks.twist.core.execution.ant.ExecuteScenariosTask;
import com.googlecode.tlb.support.junit.FilterFileSet;

import java.io.File;

import org.junit.Test;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.BuildException;

public class ExecuteScenariosTaskWrapper extends ExecuteScenariosTask {
    private FileSet fileset;
    private FilteredFile filteredFile;

    @Override
    public void setScenarioDir(File file) {
        filteredFile = new FilteredFile(file, fileset);
        super.setScenarioDir(filteredFile);
    }

    public void addFilterFileSet(FilterFileSet fs) throws BuildException {
        fs.setIncludes("**/*.scn");
        filteredFile.addFileSet(fs);
    }
}
