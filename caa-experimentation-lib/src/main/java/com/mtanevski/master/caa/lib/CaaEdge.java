package com.mtanevski.master.caa.lib;

import java.util.Objects;

public class CaaEdge {

    private final String from;
    private final String to;
    private final double weight;
    private final double traversalWeight;

    public CaaEdge(String from, String to, double weight, double traversalWeight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.traversalWeight = traversalWeight;
    }

    public double getWeight() {
        return weight;
    }

    public double getTraversalWeight() {
        return traversalWeight;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String toString() {
        return "{" + from + " " + to + "}@" + this.weight + "," + this.traversalWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CaaEdge)) return false;
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
