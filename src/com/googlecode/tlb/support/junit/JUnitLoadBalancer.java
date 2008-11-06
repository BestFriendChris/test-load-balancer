package com.googlecode.tlb.support.junit;

import com.googlecode.tlb.domain.LoadBalanceFactor;
import com.googlecode.tlb.domain.Range;
import org.apache.tools.ant.types.resources.FileResource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JUnitLoadBalancer {
    private int pieceIndex;
    private int splittedPieces;

    public JUnitLoadBalancer(int pieceIndex, int splittedPieces) {
        this.pieceIndex = pieceIndex;
        this.splittedPieces = splittedPieces;
    }

    public Iterator balance(Iterator iterator) {
        List<FileResource> files = new ArrayList<FileResource>();
        while (iterator.hasNext()) {
            files.add((FileResource) iterator.next());
        }

        final LoadBalanceFactor factor = new LoadBalanceFactor(pieceIndex, splittedPieces);
        Range ofResources = factor.getRangeOfResources(files.size());
        List<Object> list = ofResources.in(files.toArray(new Object[]{}));

        return list.iterator();

    }
}
