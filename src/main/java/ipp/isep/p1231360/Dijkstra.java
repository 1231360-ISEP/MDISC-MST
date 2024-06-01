package ipp.isep.p1231360;

import java.util.*;

public class Dijkstra {
    public static PathInfo[] dijkstra(double[][] graph, int[] signPoints, int assemblyPoint) {
        int numVertices = graph.length;
        PathInfo[] results = new PathInfo[signPoints.length];

        for (int k = 0; k < signPoints.length; k++) {
            int source = signPoints[k];

            double[] distances = new double[numVertices];
            int[] predecessors = new int[numVertices];
            boolean[] visited = new boolean[numVertices];

            Arrays.fill(distances, Double.MAX_VALUE);
            Arrays.fill(predecessors, -1);

            distances[source] = 0;
            PriorityQueue<VertexDistancePair> pq = new PriorityQueue<>(Comparator.comparingDouble(v -> v.distance));
            pq.add(new VertexDistancePair(source, 0));

            while (!pq.isEmpty()) {
                VertexDistancePair current = pq.poll();
                int currentVertex = current.vertex;

                if (visited[currentVertex]) continue;
                visited[currentVertex] = true;

                for (int i = 0; i < numVertices; i++) {
                    if (graph[currentVertex][i] > 0 && !visited[i]) {
                        double newDist = distances[currentVertex] + graph[currentVertex][i];
                        if (newDist < distances[i]) {
                            distances[i] = newDist;
                            predecessors[i] = currentVertex;
                            pq.add(new VertexDistancePair(i, newDist));
                        }
                    }
                }
            }

            List<Integer> path = new ArrayList<>();
            for (int at = assemblyPoint; at != -1; at = predecessors[at]) {
                path.add(at);
            }
            Collections.reverse(path);
            String pathStr = path.toString().replaceAll("[\\[\\]]", "").replace(",", " ->");
            double pathCost = distances[assemblyPoint];

            results[k] = new PathInfo(pathStr, pathCost);
        }
        return results;
    }

    private static class VertexDistancePair {
        int vertex;
        double distance;

        VertexDistancePair(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }
}