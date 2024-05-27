package ipp.isep.p1231360;

import java.util.*;

public class Graph {
    List<Vertex> vertexList;
    List<Edge> edgeList;
    private final Map<Vertex, List<Edge>> adjacencyList;

    public Graph() {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(Vertex vertex) {
        if (!this.vertexList.contains(vertex))
            this.vertexList.add(vertex);
    }

    public void removeVertex(Vertex vertex) {
        this.vertexList.remove(vertex);
    }

    public void addEdge(Vertex start, Vertex end, int distance) {
        Edge edge = new Edge(start, end, distance);
        if (!this.edgeList.contains(edge))
            this.edgeList.add(edge);
        adjacencyList.putIfAbsent(start, new ArrayList<>());
        adjacencyList.putIfAbsent(end, new ArrayList<>());
        adjacencyList.get(start).add(edge);
    }

    public void removeEdge(Edge edge) {
        this.edgeList.remove(edge);
    }

    public boolean getAdjacent(Vertex vertex1, Vertex vertex2) {
        return this.edgeList.stream().anyMatch(edge -> edge.getStart().equals(vertex1) && edge.getEnd().equals(vertex2));
    }

    public List<Vertex> getNeighbors(Vertex vertex) {
        List<Vertex> neighbors = new ArrayList<>();
        for (Edge edge : this.edgeList) {
            if (edge.getStart().equals(vertex) && !neighbors.contains(edge.getEnd())) {
                neighbors.add(edge.getEnd());
            }
        }
        return neighbors;
    }

    public void printGraph() {
        for (Vertex vertex : this.vertexList) {
            System.out.printf("Vertex %s%n", vertex.getValue());
            List<Edge> vertexEdgeList = adjacencyList.get(vertex);
            if (vertexEdgeList != null) {
                for (Edge edge : vertexEdgeList) {
                    System.out.printf("  ==[%d]==> %s%n", edge.getDistance(), edge.getEnd().getValue());
                }
            }
        }
    }

    public void printEdges() {
        for (Edge edge : this.edgeList) {
            System.out.printf("%s ==[%d]==> %s%n", edge.getStart().getValue(), edge.getDistance(), edge.getEnd().getValue());
        }
    }

    public List<Edge> getEdges(Vertex vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    public Set<Vertex> getVertices() {
        return new HashSet<>(vertexList);
    }
}
