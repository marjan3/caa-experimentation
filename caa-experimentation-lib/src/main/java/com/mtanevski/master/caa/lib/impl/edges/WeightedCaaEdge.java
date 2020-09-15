package com.mtanevski.master.caa.lib.impl.edges;

import com.mtanevski.master.caa.lib.CaaEdge;

public class WeightedCaaEdge extends CaaEdge {

    public WeightedCaaEdge(CaaEdge caaEdge) {
        super(caaEdge.getFrom(), caaEdge.getTo(), caaEdge.getWeight(), 0);
    }

    public WeightedCaaEdge(String from, String to, double weight) {
        super(from, to, weight, 0);
    }

    public static CaaEdge of(String from, String to, double weight) {
        return new WeightedCaaEdge(from, to, weight);
    }

    public String toString() {
        return "(" + this.getFrom() + "-" + this.getTo() + ")@" + this.getWeight();
    }

}
