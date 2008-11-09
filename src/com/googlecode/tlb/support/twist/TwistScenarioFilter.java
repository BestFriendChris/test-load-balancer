package com.googlecode.tlb.support.twist;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import static java.util.Arrays.sort;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import static java.lang.String.format;

import com.googlecode.tlb.domain.Range;
import com.googlecode.tlb.domain.LoadBalanceFactor;

public class TwistScenarioFilter implements TestFilter {
    private final Iterator scenarios;
    private final static String SCN_SUFFIX = ".scn";

    public static final Logger LOGGER = Logger.getLogger(TwistScenarioFilter.class);

    public TwistScenarioFilter(Iterator scenarios) {
        this.scenarios = scenarios;
    }

    public void filter(LoadBalanceFactor factor) {
        final List<File> scenarioFiles = getScenarioFiles();
        List<File> toKeep = getScenariosToKeep(factor, scenarioFiles);
        deleteScenarios(scenarioFiles, toKeep);
    }


    void deleteScenarios(List<File> scenarioFiles, List<File> toKeep) {
        log(toKeep);
        for (File scenarioFile : scenarioFiles) {
            if (!toKeep.contains(scenarioFile)) {
                try {
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

    private List getScenariosToKeep(LoadBalanceFactor factor, List<File> scenarioFiles) {
        final Range range = factor.getRangeOfResources(scenarioFiles.size());
        return range.in(scenarioFiles);
    }

    private List<File> getScenarioFiles() {
        List<File> scenarioFiles = new ArrayList<File>();
        while (scenarios.hasNext()) {
            scenarioFiles.add((File) scenarios.next());
        }
        Collections.sort(scenarioFiles, new NameFileComparator());
        return scenarioFiles;
    }
}
