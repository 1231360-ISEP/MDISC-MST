package lpschool;

import java.util.Scanner;

public class Files {
    public static void readFile(Scanner scanner, Graph graph) {
        while (scanner.hasNextLine()) {
            String[] data = scanner.nextLine().split(";");

            Vertex start = new Vertex(data[0].trim());
            Vertex end = new Vertex(data[1].trim());

            graph.addVertex(start);
            graph.addVertex(end);
            graph.addEdge(new Edge(start, end, Integer.parseInt(data[2].trim())));
        }
    }
}
