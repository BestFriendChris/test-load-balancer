package com.googlecode.tlb.support.twist;

import com.googlecode.tlb.domain.LoadBalanceFactor;
import com.googlecode.tlb.domain.LoadBalancer;

import java.util.Iterator;

public class TwistLoadBalancer implements LoadBalancer {
    private int pieceIndex;
    private int splittedPieces;


    public TwistLoadBalancer() {
    }

    public Iterator balance(Iterator iterator, int splittedPieces, int pieceIndex) {
        this.pieceIndex = pieceIndex;
        this.splittedPieces = splittedPieces;
        final LoadBalanceFactor factor = new LoadBalanceFactor(this.pieceIndex, this.splittedPieces);
        new TwistScenarioFilter(iterator).filter(factor);
        // TODO - yanghada - fix this
        return null;
    }
}
