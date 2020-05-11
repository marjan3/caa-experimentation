package com.mtanevski.master.lib.caa.impl.graph.readonly;

import com.mtanevski.master.lib.caa.CaaEdge;

import java.util.Objects;

public class EmptyCaaEdge implements CaaEdge {

    public EmptyCaaEdge() {
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
        return null;
    }

    @Override
    public String getFrom() {
        return null;
    }

    @Override
    public String toString() {
        return "{}";
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
