package com.googlecode.tlb;

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
        if (areJobsMoreThanTests(allTestResourse)) {
            return assignEachJobOneTest(allTestResourse);
        } else {
            int amount = averageWithoutMod(allTestResourse);
            return new Range((thisJobIndex - 1) * amount, getLength(allTestResourse, amount));
        }

    }

    private boolean areJobsMoreThanTests(int allTestResourse) {
        return allTestResourse < this.allJobCounts;
    }

    private Range assignEachJobOneTest(int allTestResourse) {
        if (noMoreTestToAssign(allTestResourse)) {
            return new NullRange();
        } else {
            return new Range(thisJobIndex - 1, 1);
        }
    }

    private boolean noMoreTestToAssign(int allTestResourse) {
        return this.thisJobIndex > allTestResourse;
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
