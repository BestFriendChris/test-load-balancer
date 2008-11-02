package com.googlecode.tlb;

import org.apache.log4j.Logger;

import static java.lang.String.format;

public class LoadBalanceFactor {
    private static final Logger LOGGER = Logger.getLogger(LoadBalanceFactor.class);
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
        return format("%d/%d", thisJobIndex, allJobCounts);
    }

    public Range amountOfTests(int allTestResourse) {
        Range range = null;
        if (areJobsMoreThanTests(allTestResourse)) {
            range = assignEachJobOneTest(allTestResourse);
        } else {
            int amount = averageWithoutMod(allTestResourse);
            range = new Range((thisJobIndex - 1) * amount, getLength(allTestResourse, amount));
        }
        final String msg = format("[%d] tests to load balance between [%d] jobs. Assigned range [%s] for #[%d] job",
                allTestResourse, allJobCounts, range, thisJobIndex);
        LOGGER.info(msg);
        System.out.println(msg);
        return range;
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
