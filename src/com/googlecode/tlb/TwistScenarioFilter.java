package com.googlecode.tlb;

import org.apache.commons.io.comparator.NameFileComparator;

import java.io.File;
import java.io.FilenameFilter;
import static java.util.Arrays.sort;
import java.util.List;

public class TwistScenarioFilter implements TestFilter {
    private final File scenarioDir;
    private final static String SCN_SUFFIX = ".scn";

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

    private void deleteScenarios(File[] scenarioFiles, List<File> toKeep) {
        for (File scenarioFile : scenarioFiles) {
            if (!toKeep.contains(scenarioFile)) {
                scenarioFile.delete();
            }
        }
    }

    private List<File> getScenariosToKeep(LoadBalanceFactor factor, File[] scenarioFiles) {
        final Range range = factor.amountOfTests(scenarioFiles.length);
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
