package com.googlecode.tlb.domain;

import org.apache.log4j.Logger;

import static java.lang.String.format;

public class LoadBalanceFactor {
    private static final Logger LOGGER = Logger.getLogger(LoadBalanceFactor.class);
    private int pieceIndex;
    private int splittedPieces;

    public LoadBalanceFactor(int pieceIndex, int splittedPieces) {
        this.pieceIndex = pieceIndex;
        this.splittedPieces = splittedPieces;
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

    public Range getRangeOfResources(int allTestResourse) {
        Range range;
        if (areJobsMoreThanTests(allTestResourse)) {
            range = assignEachJobOneTest(allTestResourse);
        } else {
            int mod = getMod(allTestResourse);
            int startIndex;
            int length;
            if (mod == 0) {
                length = averageWithoutMod(allTestResourse, mod);
                startIndex = (pieceIndex - 1) * length;
            } else if (pieceIndex - 1 < mod) {
                length = averageWithoutMod(allTestResourse, mod) + 1;
                startIndex =(pieceIndex - 1) * length;
            } else {
                length = averageWithoutMod(allTestResourse, mod);
                startIndex = (length + 1) * mod + (pieceIndex - mod - 1) * length;   
            }
            range = new Range(startIndex, length);
        }
        final String msg = format("[%d] tests to load balance between [%d] jobs. Assigned range [%s] to #[%d] job",
                allTestResourse, splittedPieces, range, pieceIndex);
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
