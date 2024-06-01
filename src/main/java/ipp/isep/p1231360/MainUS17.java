package ipp.isep.p1231360;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainUS17 {
    private static final String INPUT_MATRIX_PATH = "src/main/resources/in/us17/us17_matrix.csv";
    private static final String INPUT_NAMES_PATH = "src/main/resources/in/us17/us17_points_names.csv";
    private static final String OUTPUT_CSV_PATH = "src/main/resources/out/us17/shortest_paths.csv";
    private static final String GRAPH_PATH = "src/main/resources/out/us17/graph.gv";
    private static final String PATH_IMAGE_PATH = "src/main/resources/out/us17/path.png";

    public static void main(String[] args) throws IOException {
        double[][] graphMatrix = FilesUS17.readMatrix(new File(INPUT_MATRIX_PATH));
        String[] pointNames = FilesUS17.readPointNames(new File(INPUT_NAMES_PATH));

        List<Integer> signPoints = new ArrayList<>();
        int assemblyPoint = -1;

        for (int i = 0; i < pointNames.length; i++) {
            if (pointNames[i].contains("AP")) {
                assemblyPoint = i;
            } else {
                signPoints.add(i);
            }
        }

        if (assemblyPoint == -1) {
            throw new IllegalStateException("No assembly point found in the input names.");
        }

        int[] signPointsArray = signPoints.stream().mapToInt(Integer::intValue).toArray();
        PathInfo[] paths = Dijkstra.dijkstra(graphMatrix, signPointsArray, assemblyPoint);

        // Encontra o caminho de menor custo entre os pontos de sinalização e o ponto de montagem
        PathInfo shortestPath = findShortestPath(paths);

        // Escreve os caminhos no arquivo de saída
        writePathsToFile(paths, shortestPath);

        Graph graph = new SingleGraph("Graph");

        for (int i = 0; i < pointNames.length; i++) {
            graph.addNode(pointNames[i]).setAttribute("ui.label", pointNames[i]);
        }

        for (int i = 0; i < graphMatrix.length; i++) {
            for (int j = i + 1; j < graphMatrix[i].length; j++) {
                if (graphMatrix[i][j] > 0) {
                    org.graphstream.graph.Edge graphEdge = graph.addEdge(pointNames[i] + "-" + pointNames[j], pointNames[i], pointNames[j]);
                    graphEdge.setAttribute("distance", graphMatrix[i][j]);
                    graphEdge.setAttribute("label", graphMatrix[i][j]);
                }
            }
        }

        // Sublinhe o caminho com cor vermelha
        String[] shortestPathNodes = shortestPath.path.split(" -> ");
        for (int i = 0; i < shortestPathNodes.length - 1; i++) {
            String edgeId = shortestPathNodes[i] + "-" + shortestPathNodes[i + 1];
            if (graph.getEdge(edgeId) != null) {
                graph.getEdge(edgeId).setAttribute("color", "red");
            } else {
                // Verifica se a direção inversa da aresta existe
                edgeId = shortestPathNodes[i + 1] + "-" + shortestPathNodes[i];
                if (graph.getEdge(edgeId) != null) {
                    graph.getEdge(edgeId).setAttribute("color", "red");
                }
            }
        }

        FileSinkDOT sink = new FileSinkDOT();
        sink.writeAll(graph, GRAPH_PATH);

        String[] command = {"dot", "-Tpng", GRAPH_PATH, "-o", PATH_IMAGE_PATH};
        Runtime.getRuntime().exec(command);
    }

    private static PathInfo findShortestPath(PathInfo[] paths) {
        PathInfo shortestPath = paths[0]; // Assume o primeiro como o mais curto inicialmente
        for (int i = 1; i < paths.length; i++) {
            if (paths[i].distance < shortestPath.distance) {
                shortestPath = paths[i];
            }
        }
        return shortestPath;
    }

    private static void writePathsToFile(PathInfo[] paths, PathInfo shortestPath) throws IOException {
        File outputCSVFile = new File(OUTPUT_CSV_PATH);
        if (!outputCSVFile.exists()) outputCSVFile.createNewFile();

        if (!outputCSVFile.isFile() || !outputCSVFile.canWrite()) {
            throw new FileNotFoundException(OUTPUT_CSV_PATH + " file not found");
        }

        try (Writer outputCSVFileWriter = new FileWriter(outputCSVFile)) {
            for (PathInfo pathInfo : paths) {
                outputCSVFileWriter.write(pathInfo.path + ". Cost: " + pathInfo.distance + "\n");
            }

            // Adiciona a linha extra com o caminho de menor custo
            outputCSVFileWriter.write("\nShortest Path: " + shortestPath.path + ". Cost: " + shortestPath.distance);
        }
    }
}
