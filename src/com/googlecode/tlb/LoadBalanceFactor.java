package com.googlecode.tlb;

/**
 *
 */
public class LoadBalanceFactor {
    private String thisJobIndex;
    private String allJobCounts;

    public LoadBalanceFactor(String thisJobIndex, String allJobCounts) {
        this.thisJobIndex = thisJobIndex;
        this.allJobCounts = allJobCounts;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoadBalanceFactor that = (LoadBalanceFactor) o;

        if (allJobCounts != null ? !allJobCounts.equals(that.allJobCounts) : that.allJobCounts != null) {
            return false;
        }
        if (thisJobIndex != null ? !thisJobIndex.equals(that.thisJobIndex) : that.thisJobIndex != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (thisJobIndex != null ? thisJobIndex.hashCode() : 0);
        result = 31 * result + (allJobCounts != null ? allJobCounts.hashCode() : 0);
        return result;
    }
}
