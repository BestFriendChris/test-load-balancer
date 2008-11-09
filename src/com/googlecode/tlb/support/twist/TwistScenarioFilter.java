package com.googlecode.tlb.support.twist;

import com.googlecode.tlb.domain.Range;
import org.apache.log4j.Logger;
import org.apache.commons.io.comparator.NameFileComparator;

import java.io.File;
import static java.lang.String.format;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;

public class TwistScenarioFilter {
    public static final Logger LOGGER = Logger.getLogger(TwistScenarioFilter.class);

    private final List<File> scenarios;

    public TwistScenarioFilter(List<File> scenarios) {
        this.scenarios = sort(scenarios);
    }

    private List<File> sort(List<File> scenarioFiles) {
        Collections.sort(scenarioFiles, new NameFileComparator());
        return scenarioFiles;
    }

    public Iterator<File> filter(Range range) {
        List<File> toKeep = getScenariosToKeep(range);
        deleteScenarios(scenarios, toKeep);
        return toKeep.iterator();
    }

    private List getScenariosToKeep(Range range) {
        return range.in(this.scenarios);
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
}
