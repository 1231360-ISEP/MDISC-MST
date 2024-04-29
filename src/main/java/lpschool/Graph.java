package lpschool;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Graph {
    public final List<Vertex> vertexList;
    public final List<Edge> edgeList;

    public Graph() {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
    }

    public void addVertex(Vertex vertex) {
        if(!this.vertexList.contains(vertex))
            this.vertexList.add(vertex);
    }

    public void removeVertex(Vertex vertex) {
        this.vertexList.remove(vertex);
    }

    public void addEdge(Edge edge) {
        if(!this.edgeList.contains(edge))
            this.edgeList.add(edge);
    }

    public void removeEdge(Edge edge) {
        this.edgeList.remove(edge);
    }

    public boolean getAdjacent(Vertex vertex1, Vertex vertex2) {
        for (Edge edge : this.edgeList) {
            if(edge.getStart().equals(vertex1) && edge.getEnd().equals(vertex2)) {
                return true;
            }
        }

        return false;
    }

    public List<Vertex> getNeighbors(Vertex vertex) {
        List<Vertex> neighbors = new ArrayList<>();

        for (Edge edge : this.edgeList) {
            if (edge.getStart().equals(vertex)) {
                if (!neighbors.contains(edge.getEnd())) {
                    neighbors.add(edge.getEnd());
                }
            }
        }

        return neighbors;
    }

    public void printGraph() {
        for (Vertex vertex : this.vertexList) {
            System.out.printf("Vertex %s%n", vertex.getValue());

            List<Edge> vertexEdgeList = edgeList.stream().filter(edge -> {
                return edge.getStart().equals(vertex);
            }).toList();

            for (Edge edge : vertexEdgeList) {
                System.out.printf("  ==[%d]==> %s%n", edge.getDistance(), edge.getEnd().getValue());
            }
        }
    }

    public void printEdges() {
        for (Edge edge : this.edgeList) {
            System.out.printf("%s ==[%d]==> %s%n", edge.getStart().getValue(), edge.getDistance(), edge.getEnd().getValue());
        }
    }
}
