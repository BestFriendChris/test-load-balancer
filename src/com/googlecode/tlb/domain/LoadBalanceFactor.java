package com.googlecode.tlb.domain;

import org.apache.log4j.Logger;

import static java.lang.String.format;

import com.googlecode.tlb.domain.Range;
import com.googlecode.tlb.domain.NullRange;

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
        Range range = null;
        if (areJobsMoreThanTests(allTestResourse)) {
            range = assignEachJobOneTest(allTestResourse);
        } else {
            int amount = averageWithoutMod(allTestResourse);
            range = new Range((pieceIndex - 1) * amount, getLength(allTestResourse, amount));
        }
        final String msg = format("[%d] tests to load balance between [%d] jobs. Assigned range [%s] for #[%d] job",
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

    private int getLength(int allTestResourse, int evenResult) {
        final int mod = getMod(allTestResourse);
        return isLastJob() ? evenResult + mod : evenResult;
    }

    private int getMod(int allTestResourse) {
        return allTestResourse % splittedPieces;
    }

    private int averageWithoutMod(int allTestResourse) {
        if (pieceIndex == 0 || splittedPieces == 0) {
            throw new RuntimeException("Invalid load balance factor: " + this);
        }
        final int mod = getMod(allTestResourse);
        return (allTestResourse - mod) / getSplittedPieces();
    }

    private boolean isLastJob() {
        return pieceIndex == splittedPieces;
    }
}
