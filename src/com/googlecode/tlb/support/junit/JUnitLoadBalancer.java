package com.googlecode.tlb.support.junit;

import com.googlecode.tlb.domain.LoadBalanceFactor;
import com.googlecode.tlb.domain.LoadBalancer;
import com.googlecode.tlb.domain.Range;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JUnitLoadBalancer implements LoadBalancer {

    public Iterator balance(Iterator testResources, int splittedPieces, int pieceIndex) {
        List testFiles = listFiles(testResources);

        final LoadBalanceFactor factor = new LoadBalanceFactor(testFiles.size(), splittedPieces, pieceIndex);
        Range ofResources = factor.getRangeOfResources();

        return ofResources.in(testFiles).iterator();
    }

    private List listFiles(Iterator iterator) {
        List files = new ArrayList();
        while (iterator.hasNext()) {
            files.add(iterator.next());
        }
        return files;
    }
}
