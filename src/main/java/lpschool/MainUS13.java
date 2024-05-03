package lpschool;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.IOException;

public class MainUS13 {
    private static final String GRAPH_OUT_PATH = "src/main/resources/out/us13/graph.txt";

    public static void main(String[] args) throws IOException {
        Graph graph = new MultiGraph("Grafos");

        Node node1 = graph.addNode("A");
        Node node2 = graph.addNode("B");

        graph.addEdge("AB", node1, node2);
        graph.addEdge("BA", node1, node2);

        System.setProperty("org.graphstream.ui", "swing");

        graph.display();

        FileSinkDOT sink = new FileSinkDOT();

        sink.writeAll(graph, GRAPH_OUT_PATH);
    }
}
