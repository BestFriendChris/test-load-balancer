package com.googlecode.tlb.support.twist;

import com.thoughtworks.twist.core.execution.ant.ExecuteScenariosTask;

import java.io.File;

import org.junit.Test;
import org.apache.tools.ant.types.FileSet;

public class ExecuteScenariosTaskWrapper extends ExecuteScenariosTask {
    private FileSet fileset;
    private FilteredFile filteredFile;

    @Override
    public void setScenarioDir(File file) {
        filteredFile = new FilteredFile(file, fileset);
        super.setScenarioDir(filteredFile);
    }

    public void addFileset(FileSet fileset) {
        filteredFile.addFileSet(fileset);
    }
}
