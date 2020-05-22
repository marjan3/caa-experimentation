package com.mtanevski.master.caa.gui.web.visjs;

import java.util.Objects;

public class VisNode {

    private int id;
    private String label;

    public VisNode(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisNode visNode = (VisNode) o;
        return getId() == visNode.getId() &&
                Objects.equals(getLabel(), visNode.getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLabel());
    }
}
