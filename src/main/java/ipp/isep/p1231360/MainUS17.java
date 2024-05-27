package ipp.isep.p1231360;

import java.io.*;
import java.util.*;

public class MainUS17 {
    private static final String INPUT_PATH = "src/main/resources/in/us17";
    private static final String OUTPUT_PATH = "src/main/resources/out/us17";
    private static final String OUTPUT_CSV_PATH = OUTPUT_PATH + "/us17.csv";

    public static void main(String[] args) throws IOException {
        File inputFile = new File(INPUT_PATH + "/us17_input.csv");
        if (!inputFile.exists()) {
            throw new FileNotFoundException(INPUT_PATH + " file not found");
        }

        Graph graph = new Graph();
        Scanner fileIn = new Scanner(inputFile);
        while (fileIn.hasNextLine()) {
            String line = fileIn.nextLine();
            String[] values = line.split(",");
            String startValue = values[0].trim();
            String endValue = values[1].trim();
            int distance = Integer.parseInt(values[2].trim());
            Vertex start = new Vertex(startValue);
            Vertex end = new Vertex(endValue);
            graph.addVertex(start);
            graph.addVertex(end);
            graph.addEdge(start, end, distance);
        }
        fileIn.close();

        // Assuming Assembly Point is the last vertex
        Vertex assemblyPoint = graph.getVertices().stream().max(Comparator.comparing(Vertex::getValue)).orElseThrow();

        // Find shortest paths from all vertices to the Assembly Point
        Map<Vertex, PathInfo> allPaths = new HashMap<>();
        for (Vertex vertex : graph.getVertices()) {
            allPaths.put(vertex, Dijkstra.dijkstra(graph, vertex, assemblyPoint));
        }

        // Write results to the output CSV file
        writeCSV(allPaths, OUTPUT_CSV_PATH);

        // Generate dot file for visualization
        generateDotFile(graph, OUTPUT_PATH + "/graph.dot");
    }

    public static void writeCSV(Map<Vertex, PathInfo> paths, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry<Vertex, PathInfo> entry : paths.entrySet()) {
                Vertex vertex = entry.getKey();
                PathInfo pathInfo = entry.getValue();
                writer.write(vertex.getValue() + ";" + pathInfo.path + ";" + pathInfo.distance + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateDotFile(Graph graph, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("digraph G {\n");
            for (Vertex vertex : graph.getVertices()) {
                for (Edge edge : graph.getEdges(vertex)) {
                    writer.write("  " + edge.getStart().getValue() + " -> " + edge.getEnd().getValue() + " [label=\"" + edge.getDistance() + "\"];\n");
                }
            }
            writer.write("}\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
