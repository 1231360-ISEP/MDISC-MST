package lpschool;

import java.util.List;

public class Kruskal {
    private static class Subset {
        public int parent;
        public int rank;

        public Subset(int parent, int rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }

    private static int findRoot(Subset[] subsets, int element) {
        if (subsets[element].parent == element)
            return subsets[element].parent;

        subsets[element].parent = findRoot(subsets, subsets[element].parent);

        return subsets[element].parent;
    }

    private static void union(Subset[] subsets, int x, int y) {
        int rootX = findRoot(subsets, x);
        int rootY = findRoot(subsets, y);

        if(subsets[rootY].rank < subsets[rootX].rank) {
            subsets[rootY].parent = rootX;
        } else if (subsets[rootX].rank < subsets[rootY].rank) {
            subsets[rootX].parent = rootY;
        }else {
            subsets[rootY].parent = rootX;
            subsets[rootX].rank++;
        }
    }

    public static int kruskal(Graph graph, Edge[] result) {
        List<Vertex> vertices = graph.vertexList;
        List<Edge> edges = graph.edgeList;
        int vertexCount = vertices.size();

        int j = 0;
        int nEdges = 0;

        Subset[] subsets = new Subset[vertexCount];
        result = new Edge[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            subsets[i] = new Subset(i, 0);
        }

        while (nEdges < vertexCount - 1) {
            Edge nextEdge = edges.get(j);

            int x = findRoot(subsets, vertices.indexOf(nextEdge.getStart()));
            int y = findRoot(subsets, vertices.indexOf(nextEdge.getEnd()));

            if (x != y) {
                result[nEdges] = nextEdge;
                union(subsets, x, y);
                nEdges++;
            }

            j++;
        }

        int cost = 0;

        for (int i = 0; i < nEdges; i++)
            cost += result[i].getDistance();

        return cost;
    }
}
