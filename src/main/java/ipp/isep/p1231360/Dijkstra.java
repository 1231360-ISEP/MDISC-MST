package ipp.isep.p1231360;

import java.util.*;

class Dijkstra {
    public static PathInfo dijkstra(Graph graph, Vertex source, Vertex target) {
        Map<Vertex, Integer> distances = new HashMap<>();
        Map<Vertex, Vertex> predecessors = new HashMap<>();
        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (Vertex vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
            predecessors.put(vertex, null);
        }
        distances.put(source, 0);
        priorityQueue.add(source);

        while (!priorityQueue.isEmpty()) {
            Vertex current = priorityQueue.poll();

            if (current.equals(target)) {
                break;
            }

            for (Edge edge : graph.getEdges(current)) {
                Vertex neighbor = edge.getEnd();
                int newDist = distances.get(current) + edge.getDistance();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    priorityQueue.add(neighbor);
                }
            }
        }

        List<Vertex> path = new ArrayList<>();
        for (Vertex at = target; at != null; at = predecessors.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        String pathStr = path.toString().replaceAll("[\\[\\]]", "");
        int totalDistance = distances.get(target);

        return new PathInfo(pathStr, totalDistance);
    }
}
