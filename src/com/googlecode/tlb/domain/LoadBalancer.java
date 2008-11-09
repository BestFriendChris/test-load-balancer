package com.googlecode.tlb.domain;

import java.util.Iterator;

public interface LoadBalancer {
    Iterator balance(Iterator testResources, int splittedPieces, int pieceIndex);
}
