package com.thoughtworks.twist.core.execution.ant;

import java.io.File;
import java.io.FileFilter;

import com.thoughtworks.twist.core.Tags;
import com.thoughtworks.twist.core.parser.ScenarioParser;

public class TagFileFilter implements FileFilter {

    private final ScenarioParser parser = new ScenarioParser();
    private final Tags tags;

    public TagFileFilter(String tags) {
        this.tags = Tags.toTags(tags);
    }

    public TagFileFilter(Tags tags) {
        this.tags = tags;
    }

    public boolean accept(File pathname) {
        return isScenario(pathname) && hasTags(pathname);
    }

    private boolean hasTags(File pathname) {
        return new Tags(parser.parse(pathname)).hasTags(tags); // TODO Check. intersects??
    }

    private boolean isScenario(File pathname) {
        return pathname.isFile() && pathname.getAbsolutePath().endsWith(".scn");
    }
}