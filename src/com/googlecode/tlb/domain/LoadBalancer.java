package com.googlecode.tlb.domain;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;

import java.io.ByteArrayInputStream;
import java.io.File;

import com.googlecode.tlb.parser.GroupLexer;
import com.googlecode.tlb.parser.GroupParser;
import com.googlecode.tlb.support.twist.Group;
import com.googlecode.tlb.support.twist.Groups;
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
