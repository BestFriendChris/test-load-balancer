package com.googlecode.tlb.domain;

import org.apache.log4j.Logger;

import static java.lang.String.format;

public class LoadBalanceFactor {
    private static final Logger LOGGER = Logger.getLogger(LoadBalanceFactor.class);
    private int pieceIndex;
    private int splittedPieces;
    private int allTestResources;

    public LoadBalanceFactor(int allTestResources, int splittedPieces, int pieceIndex) {
        this.splittedPieces = splittedPieces;
        this.pieceIndex = pieceIndex;
        this.allTestResources = allTestResources;
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
        if (areJobsMoreThanTests(allTestResources)) {
            range = assignEachJobOneTest(allTestResources);
        } else {
            int mod = getMod(allTestResources);
            int startIndex;
            int length;
            if (mod == 0) {
                length = averageWithoutMod(allTestResources, mod);
                startIndex = (pieceIndex - 1) * length;
            } else if (pieceIndex - 1 < mod) {
                length = averageWithoutMod(allTestResources, mod) + 1;
                startIndex =(pieceIndex - 1) * length;
            } else {
                length = averageWithoutMod(allTestResources, mod);
                startIndex = (length + 1) * mod + (pieceIndex - mod - 1) * length;   
            }
            range = new Range(startIndex, length);
        }
        final String msg = format("[%d] tests to load balance between [%d] jobs. Assigned range [%s] to #[%d] job",
                allTestResources, splittedPieces, range, pieceIndex);
        LOGGER.info(msg);
        System.out.println(msg);
        return range;
    }

    private boolean areJobsMoreThanTests(int amountOfTest) {
        return amountOfTest < this.splittedPieces;
    }

    private Range assignEachJobOneTest(int allTestResourse) {
        if (noMoreTestToAssign(allTestResourse)) {
            return new NullRange();
        } else {
            return new Range(pieceIndex - 1, 1);
        }
    }

    private boolean noMoreTestToAssign(int allTestResourse) {
        return this.pieceIndex > allTestResourse;
    }

    private int getMod(int allTestResourse) {
        return allTestResourse % splittedPieces;
    }

    private int averageWithoutMod(int allTestResourse, int mod) {
        if (pieceIndex == 0 || splittedPieces == 0) {
            throw new RuntimeException("Invalid load balance factor: " + this);
        }
        return (allTestResourse - mod) / getSplittedPieces();
    }
}
