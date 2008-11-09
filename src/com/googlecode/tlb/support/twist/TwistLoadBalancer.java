package com.googlecode.tlb.support.twist;

import com.googlecode.tlb.domain.LoadBalanceFactor;
import com.googlecode.tlb.domain.LoadBalancer;
import com.googlecode.tlb.domain.Range;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;

import org.apache.commons.io.comparator.NameFileComparator;

public class TwistLoadBalancer implements LoadBalancer {

    public Iterator balance(Iterator iterator, int splittedPieces, int pieceIndex) {
        List<File> scenarioFiles = getScenarioFiles(iterator);

        final LoadBalanceFactor factor = new LoadBalanceFactor(scenarioFiles.size(), splittedPieces, pieceIndex);
        Range range = factor.getRangeOfResources();

        return new TwistScenarioFilter(scenarioFiles).filter(range);
    }

    private List<File> getScenarioFiles(Iterator iterator) {
        List<File> scenarioFiles = new ArrayList<File>();
        while (iterator.hasNext()) {
            scenarioFiles.add((File) iterator.next());
        }
        return scenarioFiles;
    }
}
