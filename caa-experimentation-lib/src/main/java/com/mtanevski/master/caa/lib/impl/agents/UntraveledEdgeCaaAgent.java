package com.mtanevski.master.caa.lib.impl.agents;

import com.mtanevski.master.caa.lib.CaaAgent;
import com.mtanevski.master.caa.lib.CaaEdge;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Selects edge based on the weight
 */
public class UntraveledEdgeCaaAgent implements CaaAgent {

    private static final Random randomizer = new Random();

    @Override
    public Optional<CaaEdge> selectEdge(List<CaaEdge> adjacentEdges) {
        if (adjacentEdges == null) {
            throw new IllegalArgumentException("Can not select an edge from a null list of edges");
        }
        return adjacentEdges.stream().max((edge1, edge2) -> {
            // if the desired state factor has already been increased
            // that means that edge leads to the desired state
            // choose based on whether the path has been traversed
            // always pick the least traversed one
            // that way we always look for new ways to reach
            if (edge1.getWeight() != edge2.getWeight()) {
                return Double.compare(edge1.getWeight(), edge2.getWeight());
            }

            // if the filter above is applied it will improve the number of generations
            if (edge1.getTraversalWeight() != edge2.getTraversalWeight()) {
                return Double.compare(edge1.getTraversalWeight(), edge2.getTraversalWeight());
            }

            // if none of the previous methods worked, choose edge randomly
            return randomizer.nextInt();
        });
    }
}
