package com.googlecode.tlb.support.twist;

import com.googlecode.tlb.support.junit.JUnitLoadBalancer;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.types.resources.FileResource;
import org.apache.commons.io.FileUtils;

public class TwistLoadBalancer extends JUnitLoadBalancer {
    private static final Pattern MANUAL_SCENARIO =
            Pattern.compile(".*^Metadata:.*ScenarioType=Manual.*", Pattern.MULTILINE | Pattern.DOTALL);

    @Override
    protected List listFiles(Iterator iterator) {
        ArrayList files = new ArrayList();
        while (iterator.hasNext()) {
            FileResource resource = (FileResource) iterator.next();
            File file = resource.getFile();
            try {
                String s = FileUtils.readFileToString(file);
                if (!MANUAL_SCENARIO.matcher(s).matches()) { files.add(resource); }
            } catch (IOException e) {
                throw new RuntimeException("Unable to read file " + file.getAbsolutePath(), e);
            }
        }
        return files;
    }
}
