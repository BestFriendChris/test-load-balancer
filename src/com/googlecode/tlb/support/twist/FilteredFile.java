package com.googlecode.tlb.support.twist;

import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.resources.FileResource;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//this class is designed to hack the implementation for test since twist does not support fileset which is lame.
public class FilteredFile extends File {
    private FileSet fileSet;

    //scenarioDir.getAbsoluteFile().listFiles(new TagFileFilter(tags));
    public FilteredFile(File file, FileSet fileSet) {
        super(file.getAbsolutePath());
        this.fileSet = fileSet;
    }

    @Override
    public File getAbsoluteFile() {
        return this;
    }

    @Override
    public File[] listFiles(FileFilter fileFilter) {
        Iterator iterator = fileSet.iterator();
        List<File> files = new ArrayList<File>();
        while (iterator.hasNext()) {
            files.add(((FileResource) iterator.next()).getFile());
        }
        return files.toArray(new File[]{});
    }
}
