package lpschool;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MainUS13 {
    private static final String INPUT_PATH = "src/main/resources/in/us13";
    private static final String OUTPUT_PATH = "src/main/resources/out/us13";
    private static final String OUTPUT_CSV_PATH = OUTPUT_PATH + "/sub-graph.csv";
    private static final String GRAPH_PATH = OUTPUT_PATH + "/graph.gv";
    private static final String SUB_GRAPH_PATH = OUTPUT_PATH + "/sub-graph.gv";
    private static final String GRAPH_IMAGE_PATH = OUTPUT_PATH + "/graph.png";
    private static final String SUB_GRAPH_IMAGE_PATH = OUTPUT_PATH + "/sub-graph.png";

    public static void main(String[] args) throws IOException {
        File dir = new File(INPUT_PATH);

        if(!dir.isDirectory())
            throw new FileNotFoundException(INPUT_PATH + " directory not found");

        File outputDir = new File(OUTPUT_PATH);

        if (!outputDir.exists())
            outputDir.mkdirs();

        if (!outputDir.isDirectory())
            throw new FileNotFoundException(OUTPUT_PATH + " directory not found");

        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(!pathname.isFile())
                    return false;

                if(!pathname.canRead())
                    return false;

                if(!pathname.getName().endsWith(".csv"))
                    return false;

                return true;
            }
        });

        if (files == null || files.length < 1)
            return;

        int option = 0;

        do {
            for (int i = 0; i < files.length; i++) {
                System.out.printf("%3d: %s%n", i + 1, files[i].getName());
            }

            Scanner in = new Scanner(System.in);

            System.out.print("Option: ");

            option = in.nextInt();
        } while(option < 1 || option > files.length);

        File file = files[option - 1];

        Scanner fileIn = new Scanner(file);

        FileSinkDOT sink = new FileSinkDOT();

        Graph graph = new Graph();

        Files.readFile(fileIn, graph);
        
        org.graphstream.graph.Graph originalGraph = new SingleGraph("Original graph");
        org.graphstream.graph.Graph subGraph = new SingleGraph("Sub graph");

        for (Vertex vertex : graph.vertexList) {
            originalGraph.addNode(vertex.toString());
            subGraph.addNode(vertex.toString());
        }

        for (Edge edge : graph.edgeList) {
            org.graphstream.graph.Edge graphEdge = originalGraph.addEdge(edge.toString(), edge.getStart().toString(), edge.getEnd().toString());
            graphEdge.setAttribute("distance", edge.getDistance());
            graphEdge.setAttribute("label", edge.getDistance());
        }

        Collections.sort(graph.edgeList);

        Edge[] subGraphEdgeList = Kruskal.kruskalSubGraph(graph);

        File outputCSVFile = new File(OUTPUT_CSV_PATH);

        if (!outputCSVFile.exists())
            outputCSVFile.createNewFile();

        if (!outputCSVFile.isFile() || !outputCSVFile.canWrite())
            throw new FileNotFoundException(OUTPUT_CSV_PATH + " file not found");

        Writer outputCSVFileWriter = new FileWriter(outputCSVFile);

        for (Edge edge : subGraphEdgeList)
            if (edge != null)
                outputCSVFileWriter.append(String.format("%s;%s;%d%n", edge.getStart().toString(), edge.getEnd().toString(), edge.getDistance()));

        outputCSVFileWriter.close();

        for (Edge edge : subGraphEdgeList)
            if (edge != null) {
                org.graphstream.graph.Edge graphEdge = subGraph.addEdge(edge.toString(), edge.getStart().toString(), edge.getEnd().toString());
                graphEdge.setAttribute("distance", edge.getDistance());
                graphEdge.setAttribute("label", edge.getDistance());
            }

        originalGraph.edges().forEach(edge -> {
            subGraph.edges().forEach(subEdge -> {
                if (edge.getId().equals(subEdge.getId()))
                    edge.setAttribute("color", "red");
            });
        });

        List<Integer> numeros = new ArrayList<>();

        sink.writeAll(originalGraph, GRAPH_PATH);
        sink.writeAll(subGraph, SUB_GRAPH_PATH);

        String[] command1 = { "dot", "-Tpng", GRAPH_PATH, "-o", GRAPH_IMAGE_PATH };
        String[] command2 = { "dot", "-Tpng", SUB_GRAPH_PATH, "-o", SUB_GRAPH_IMAGE_PATH };
        Runtime.getRuntime().exec(command1);
        Runtime.getRuntime().exec(command2);
    }
}
