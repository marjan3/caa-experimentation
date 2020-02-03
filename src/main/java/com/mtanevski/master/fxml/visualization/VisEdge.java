package com.mtanevski.master.fxml.visualization;

import java.util.Objects;

public class VisEdge {

    private int id;
    private int from;
    private int to;
    private Integer width;

    public VisEdge(int id, VisNode from, VisNode to) {
        this.id = id;
        this.from = from.getId();
        this.to = to.getId();
    }

    public VisEdge(int id, int from, int to, int value) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.setWidth(value);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(int value) {
        this.width = value != 0 ? value * 10 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisEdge visEdge = (VisEdge) o;
        return getId() == visEdge.getId() &&
                getFrom() == visEdge.getFrom() &&
                getTo() == visEdge.getTo() &&
                Objects.equals(getWidth(), visEdge.getWidth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFrom(), getTo(), getWidth());
    }

}
