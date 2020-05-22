package com.mtanevski.master.caa.lib;

import java.util.List;
import java.util.Optional;

public interface CaaAgent {
    Optional<CaaEdge> selectEdge(List<CaaEdge> adjacentEdges);
}
