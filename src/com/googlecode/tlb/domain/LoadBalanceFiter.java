package com.googlecode.tlb.domain;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LoadBalanceFiter {
    private LoadBalanceFactor loadBalanceFactor;

    public LoadBalanceFiter(LoadBalanceFactor loadBalanceFactor) {
        this.loadBalanceFactor = loadBalanceFactor;
    }

    public List filter(List list) {
        ArrayList<Object> filtered = new ArrayList<Object>();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (loadBalanceFactor.inRange(i)) {
                filtered.add(o);
            }
        }
        return filtered;
    }

}
