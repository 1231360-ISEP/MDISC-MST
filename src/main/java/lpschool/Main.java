package lpschool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);

        System.out.printf("Indique o nome do ficheiro: ");
        String nomeFicheiro = in.next();

        File file = new File("src/" + nomeFicheiro);

        if (!file.isFile())
            throw new FileNotFoundException("Ficheiro não encontrado");

        Scanner fileIn = new Scanner(file);

        Graph graph = new Graph();

        readFile(fileIn, graph);

        Collections.sort(graph.getEdgeList());

        kruskal(graph);
    }

    private static void readFile(Scanner scanner, Graph graph) {
        while (scanner.hasNextLine()) {
            String[] data = scanner.nextLine().split(";");

            Vertex start = new Vertex(data[0].trim());
            Vertex end = new Vertex(data[1].trim());

            graph.addVertex(start);
            graph.addVertex(end);
            graph.addEdge(new Edge(start, end, Integer.parseInt(data[2].trim())));
        }
    }

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

    private static void kruskal(Graph graph) {
        List<Vertex> vertices = graph.getVertexList();
        List<Edge> edges = graph.getEdgeList();
        int vertexCount = vertices.size();

        int j = 0;
        int nEdges = 0;

        Subset[] subsets = new Subset[vertexCount];
        Edge[] result = new Edge[vertexCount];

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

        for (int i = 0; i < nEdges; i++) {
            System.out.printf("%s ==[%d]==> %s%n", result[i].getStart(), result[i].getDistance(), result[i].getEnd());

            cost += result[i].getDistance();
        }

        System.out.printf("Minimum cost = %d%n", cost);
    }
}