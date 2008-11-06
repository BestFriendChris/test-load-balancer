package com.googlecode.tlb.support.twist;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import static java.util.Arrays.sort;
import java.util.List;
import static java.lang.String.format;

import com.googlecode.tlb.domain.Range;
import com.googlecode.tlb.domain.LoadBalanceFactor;

public class TwistScenarioFilter implements TestFilter {
    private final File scenarioDir;
    private final static String SCN_SUFFIX = ".scn";

    public static final Logger LOGGER = Logger.getLogger(TwistScenarioFilter.class);

    public TwistScenarioFilter(File scenarioDir) {
        this.scenarioDir = scenarioDir;
    }

    public void filter(LoadBalanceFactor factor) {
        if (!scenarioDir.isDirectory() || !scenarioDir.exists()) {
            throw new RuntimeException(String.format("Scenario directory [%s] doesn't exist",
                    scenarioDir.getAbsolutePath()));
        }

        final File[] scenarioFiles = getScenarioFiles();
        List<File> toKeep = getScenariosToKeep(factor, scenarioFiles);
        deleteScenarios(scenarioFiles, toKeep);
    }


    void deleteScenarios(File[] scenarioFiles, List<File> toKeep) {
        log(toKeep);
        for (File scenarioFile : scenarioFiles) {
            if (!toKeep.contains(scenarioFile)) {
                try {
                    log(format("Deleting file [%s]", scenarioFile.getAbsolutePath()));
                    scenarioFile.delete();
                    log(format("File [%s] is deleted", scenarioFile.getAbsolutePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // TODO - fix logging
    private void log(String message) {
        LOGGER.info(message);
        System.out.println(message);
    }

    private void log(List<File> toKeep) {
        LOGGER.info(format("Keeping [%d] files:", toKeep.size()));
        for (File file : toKeep) {
            LOGGER.info(format("  %s", file.getAbsolutePath()));
        }
    }

    private List getScenariosToKeep(LoadBalanceFactor factor, File[] scenarioFiles) {
        final Range range = factor.getRangeOfResources(scenarioFiles.length);
        return range.in(scenarioFiles);
    }

    private File[] getScenarioFiles() {
        final File[] scenarioFiles = scenarioDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(SCN_SUFFIX);
            }
        }
        );
        sort(scenarioFiles, new NameFileComparator());
        return scenarioFiles;
    }
}
