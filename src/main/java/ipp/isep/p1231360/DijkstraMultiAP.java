package ipp.isep.p1231360;

import java.util.*;

public class DijkstraMultiAP {
    // Implementação do Dijkstra para vários AP
    public static PathInfo[] dijkstraMultiAP(double[][] graph, int[] signPoints, int[] assemblyPoints) {
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

            // Enquanto houver vértices na fila de prioridade, seleciona o vertice com menor custo,
            // verifica se existe um caminho de menor custo.
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

            // Comparação para descobrir qual é o caminho até ao AP com menor custo
            double minDistance = Double.MAX_VALUE;
            int nearestAP = -1;
            for (int ap : assemblyPoints) {
                if (distances[ap] < minDistance) {
                    minDistance = distances[ap];
                    nearestAP = ap;
                }
            }

            // Reconstrói o caminho do ponto até ao AP
            List<Integer> path = new ArrayList<>();
            for (int at = nearestAP; at != -1; at = predecessors[at]) {
                path.add(at);
            }
            Collections.reverse(path);
            String pathStr = path.toString().replaceAll("[\\[\\]]", "").replace(",", " ->");
            double pathCost = distances[nearestAP];

            results[k] = new PathInfo(pathStr, pathCost);
        }
        return results;
    }

    // Classe para representar o par vértice e a distância para a fila de prioridade
    private static class VertexDistancePair {
        int vertex;
        double distance;

        VertexDistancePair(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }
}