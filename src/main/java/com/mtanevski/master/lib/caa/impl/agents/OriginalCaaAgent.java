package com.mtanevski.master.lib.caa.impl.agents;

import com.mtanevski.master.lib.caa.CaaAgent;
import com.mtanevski.master.lib.caa.CaaEdge;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class OriginalCaaAgent implements CaaAgent {

    private static Random randomizer = new Random();

    @Override
    public Optional<CaaEdge> pickEdge(List<CaaEdge> adjacentEdges) {
        return adjacentEdges.stream().max((edge1, edge2) -> {
            // if the desired state factor has already been increased
            // that means that edge leads to the desired state
            // choose based on whether the path has been traversed
            // always pick the least traversed one
            // that way we always look for new ways to reach
            if (edge1.getWeight() != edge2.getWeight()) {
                return Double.compare(edge1.getWeight(), edge2.getWeight());
            }

            // if none of the previous methods worked, choose edge randomly
            return randomizer.nextInt();
        });
    }
}
