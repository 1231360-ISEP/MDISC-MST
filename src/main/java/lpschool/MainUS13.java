package lpschool;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

public class MainUS13 {
    private static final String INPUT_PATH = "src/main/resources/in/us13";
    private static final String OUTPUT_PATH = "src/main/resources/out/us13";
    // private static final String GRAPH_OUT_PATH = "src/main/resources/out/us13/graph.txt";

    public static void main(String[] args) throws IOException {
        File dir = new File(INPUT_PATH);

        if(!dir.isDirectory())
            throw new FileNotFoundException(INPUT_PATH + " directory not found");

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

        for (Edge edge : graph.edgeList)
            originalGraph.addEdge(edge.toString(), edge.getStart().toString(), edge.getEnd().toString()).setAttribute("distance", edge.getDistance());

        Collections.sort(graph.edgeList);

        Edge[] subGraphEdgeList = Kruskal.kruskalSubGraph(graph);

        for (Edge edge : subGraphEdgeList)
            if (edge != null)
                subGraph.addEdge(edge.toString(), edge.getStart().toString(), edge.getEnd().toString()).setAttribute("distance", edge.getDistance());

        sink.writeAll(originalGraph, OUTPUT_PATH + "/original-graph.gv");
        sink.writeAll(subGraph, OUTPUT_PATH + "/sub-graph.gv");
    }
}
