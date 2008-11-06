package com.googlecode.tlb.support.junit;

import org.apache.tools.ant.types.resources.FileResource;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import com.googlecode.tlb.domain.Range;
import com.googlecode.tlb.domain.LoadBalanceFactor;

public class JUnitLoadBalancer {
    private int pieceIndex;
    private int splittedPieces;

    public JUnitLoadBalancer(int pieceIndex, int splittedPieces) {
        this.pieceIndex = pieceIndex;
        this.splittedPieces = splittedPieces;
    }

    public Iterator balance(Iterator iterator) {
        List<File> files = new ArrayList<File>();
        while (iterator.hasNext()) {
            File file = ((FileResource) iterator.next()).getFile();
            files.add(file);
        }

        final LoadBalanceFactor factor = new LoadBalanceFactor(pieceIndex, splittedPieces);
        Range ofResources = factor.getRangeOfResources(files.size());
        List<File> list = ofResources.in(files.toArray(new File[]{}));

        List<FileResource> resources = new ArrayList<FileResource>();
        for (File file : list) {
            resources.add(new FileResource(file));
        }
        return resources.iterator();

    }
}
