package com.googlecode.tlb.support.junit;

import com.googlecode.tlb.domain.LoadBalanceFactor;
import com.googlecode.tlb.domain.LoadBalancer;
import com.googlecode.tlb.domain.Range;
import org.apache.tools.ant.types.resources.FileResource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JUnitLoadBalancer implements LoadBalancer {

    public JUnitLoadBalancer() {
    }

    public Iterator balance(Iterator iterator, int splittedPieces, int pieceIndex) {
        List files = listFiles(iterator);

        final LoadBalanceFactor factor = new LoadBalanceFactor(pieceIndex, splittedPieces);
        Range ofResources = factor.getRangeOfResources(files.size());

        return ofResources.in(files).iterator();
    }

    private List listFiles(Iterator iterator) {
        List files = new ArrayList();
        while (iterator.hasNext()) {
            files.add(iterator.next());
        }
        return files;
    }
}
