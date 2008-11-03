package com.googlecode.tlb.support.twist;

import com.googlecode.tlb.domain.LoadBalanceFactor;

/**
 *
 */
public interface TestFilter {
    public void filter(LoadBalanceFactor factor);
}
