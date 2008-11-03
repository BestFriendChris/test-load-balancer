package com.googlecode.tlb.domain;

import java.io.File;

import com.googlecode.tlb.support.twist.TwistScenarioFilter;

/**
 *
 */
public class LoadBalancer {
    private int pieceIndex;
    private int splittedPieces;


    private LoadBalancer(int pieceIndex, int splittedPieces) {
        this.pieceIndex = pieceIndex;
        this.splittedPieces = splittedPieces;
    }

    public static LoadBalancer getLoadBalancer(int pieceIndex, int splittedPieces) {
        return new LoadBalancer(pieceIndex, splittedPieces);
    }


    public void balance(File scenariosDir) {
        final LoadBalanceFactor factor = new LoadBalanceFactor(pieceIndex, splittedPieces);
        new TwistScenarioFilter(scenariosDir).filter(factor);
    }
}
