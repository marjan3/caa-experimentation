package com.mtanevski.master.lib.caa.impl.graph.tinker;

import com.mtanevski.master.lib.caa.CaaEdge;

import java.util.Objects;

public class TinkerCaaEdge implements CaaEdge {

    private final String from;
    private final String to;
    private final Double weight;
    private final Double traversalWeight;

    public TinkerCaaEdge(String from, String to, Double weight, Double traversalWeight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.traversalWeight = traversalWeight;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public double getTraversalWeight() {
        return this.traversalWeight;
    }

    @Override
    public String getTo() {
        return to;
    }

    @Override
    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return "(" + this.getFrom() + "->" + this.getTo() + ")" + this.getWeight() + "," + this.getTraversalWeight();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        CaaEdge that = (CaaEdge) o;
        return getFrom().equals(that.getFrom()) &&
                getTo().equals(that.getTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo());
    }
}
