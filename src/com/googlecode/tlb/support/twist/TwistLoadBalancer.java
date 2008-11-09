package com.googlecode.tlb.support.twist;

import com.googlecode.tlb.domain.LoadBalanceFactor;
import com.googlecode.tlb.domain.LoadBalancer;

import java.util.Iterator;

public class TwistLoadBalancer implements LoadBalancer {

    public TwistLoadBalancer() {
    }

    public Iterator balance(Iterator iterator, int splittedPieces, int pieceIndex) {
        final LoadBalanceFactor factor = new LoadBalanceFactor(pieceIndex, splittedPieces);
        new TwistScenarioFilter(iterator).filter(factor);
        // TODO - yanghada - fix this
        return null;
    }
}
