package com.googlecode.tlb;

/**
 *
 */
public class LoadBalanceFactor {
    private int thisJobIndex;
    private int allJobCounts;

    public LoadBalanceFactor(int thisJobIndex, int allJobCounts) {
        this.thisJobIndex = thisJobIndex;
        this.allJobCounts = allJobCounts;
    }

    public int getThisJobIndex() {
        return thisJobIndex;
    }

    public int getAllJobCounts() {
        return allJobCounts;
    }

    public String toString() {
        return String.format("%d/%d", thisJobIndex, allJobCounts);
    }

    public Range amountOfTests(int allTestResourse) {
        int amount = averageWithoutMod(allTestResourse);
        return new Range((thisJobIndex - 1) * amount, getLength(allTestResourse, amount));
    }

    private int getLength(int allTestResourse, int evenResult) {
        final int mod = getMod(allTestResourse);
        return isLastJob() ? evenResult + mod : evenResult;
    }

    private int getMod(int allTestResourse) {
        return allTestResourse % allJobCounts;
    }

    private int averageWithoutMod(int allTestResourse) {
        if (thisJobIndex == 0 || allJobCounts == 0) {
            throw new RuntimeException("Invalid load balance factor: " + this);
        }
        final int mod = getMod(allTestResourse);
        return (allTestResourse - mod) / getAllJobCounts();
    }

    private boolean isLastJob() {
        return thisJobIndex == allJobCounts;
    }
}
