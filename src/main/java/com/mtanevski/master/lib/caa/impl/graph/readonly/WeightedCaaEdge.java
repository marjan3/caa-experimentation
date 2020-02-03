package com.mtanevski.master.lib.caa.impl.graph.readonly;

import com.mtanevski.master.lib.caa.CaaEdge;

import java.util.Objects;

public class WeightedCaaEdge implements CaaEdge {

    private final String from;
    private final String to;
    private final double weight;

    public WeightedCaaEdge(String from, String to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public static CaaEdge of(String from, String to, double weight){
        return new WeightedCaaEdge(from, to, weight);
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public double getTraversalWeight() {
        return 0;
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
        return "{" + from + " " + to + "}@" + this.weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        CaaEdge that = (CaaEdge) o;
        return Objects.equals(getFrom(), that.getFrom()) &&
                Objects.equals(getTo(), that.getTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo());
    }
}
