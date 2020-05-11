package com.mtanevski.master.lib.caa;

import java.util.List;
import java.util.Optional;

public interface CaaAgent {
    Optional<CaaEdge> pickEdge(List<CaaEdge> adjacentEdges);
}
