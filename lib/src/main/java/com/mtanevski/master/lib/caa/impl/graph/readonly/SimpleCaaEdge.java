package com.mtanevski.master.lib.caa.impl.graph.readonly;

import com.mtanevski.master.lib.caa.CaaEdge;

import java.util.Objects;

public class SimpleCaaEdge implements CaaEdge {

    private final String from;
    private final String to;

    public SimpleCaaEdge(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public static CaaEdge of(String from, String to){
        return new SimpleCaaEdge(from, to);
    }

    @Override
    public double getWeight() {
        return 0;
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
        return "{" + from + " " + to + "}";
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
