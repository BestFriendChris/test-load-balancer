package com.googlecode.tlb.domain;

import org.apache.log4j.Logger;

import static java.lang.String.format;

public class LoadBalanceFactor {
    private static final Logger LOGGER = Logger.getLogger(LoadBalanceFactor.class);
    private int pieceIndex;
    private int splittedPieces;
    private int numberOfTests;

    public LoadBalanceFactor(int numberOfTests, int splittedPieces, int pieceIndex) {
        this.splittedPieces = splittedPieces;
        this.pieceIndex = pieceIndex;
        this.numberOfTests = numberOfTests;
    }

    public int getPieceIndex() {
        return pieceIndex;
    }

    public int getSplittedPieces() {
        return splittedPieces;
    }

    public String toString() {
        return format("%d/%d", pieceIndex, splittedPieces);
    }

    public Range getRangeOfResources() {
        Range range;
        if (arePiecesMoreThanTests(numberOfTests)) {
            range = assignEachJobOneTest(numberOfTests);
        } else {
            int mod = getMod(numberOfTests);
            int startIndex;
            int length;
            if (mod == 0) {
                length = averageWithoutMod(numberOfTests, mod);
                startIndex = (pieceIndex - 1) * length;
            } else if (pieceIndex - 1 < mod) {
                length = averageWithoutMod(numberOfTests, mod) + 1;
                startIndex =(pieceIndex - 1) * length;
            } else {
                length = averageWithoutMod(numberOfTests, mod);
                startIndex = (length + 1) * mod + (pieceIndex - mod - 1) * length;   
            }
            range = new Range(startIndex, length);
        }
        final String msg = format("[%d] tests to load balance between [%d] jobs. Assigned range [%s] to #[%d] job",
                numberOfTests, splittedPieces, range, pieceIndex);
        LOGGER.info(msg);
        System.out.println(msg);
        return range;
    }

    private boolean arePiecesMoreThanTests(int numberOfTests) {
        return numberOfTests < this.splittedPieces;
    }

    private Range assignEachJobOneTest(int numberOfTests) {
        if (noMoreTestToAssign(numberOfTests)) {
            return new NullRange();
        } else {
            return new Range(pieceIndex - 1, 1);
        }
    }

    private boolean noMoreTestToAssign(int numberOfTests) {
        return this.pieceIndex > numberOfTests;
    }

    private int getMod(int numberOfTests) {
        return numberOfTests % splittedPieces;
    }

    private int averageWithoutMod(int allTestResourse, int mod) {
        if (pieceIndex == 0 || splittedPieces == 0) {
            throw new RuntimeException("Invalid load balance factor: " + this);
        }
        return (allTestResourse - mod) / getSplittedPieces();
    }
}
