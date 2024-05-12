package ipp.isep.p1231360;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private Vertex start;
    private Vertex end;
    private int distance;

    public Edge(Vertex start, Vertex end, int distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    public Vertex getStart() {
        return start;
    }

    public void setStart(Vertex start) {
        this.start = start;
    }

    public Vertex getEnd() {
        return end;
    }

    public void setEnd(Vertex end) {
        this.end = end;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return distance == edge.distance && Objects.equals(start, edge.start) && Objects.equals(end, edge.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, distance);
    }

    @Override
    public int compareTo(Edge o) {
        return this.distance - o.getDistance();
    }

    @Override
    public String toString() {
        return start.toString() + end.toString();
    }
}
