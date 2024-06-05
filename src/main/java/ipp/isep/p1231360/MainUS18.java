package ipp.isep.p1231360;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainUS18 {
    private static final String INPUT_PATH = "src/main/resources/in/us18";
    private static final String OUTPUT_PATH = "src/main/resources/out/us18";
    private static final String INPUT_MATRIX_PATH = INPUT_PATH + "/us18_matrix.csv";
    private static final String INPUT_NAMES_PATH = INPUT_PATH + "/us18_points_names.csv";
    private static final String OUTPUT_CSV_PATH = OUTPUT_PATH + "/shortest_paths.csv";
    private static final String GRAPH_PATH = OUTPUT_PATH + "/graph.gv";
    private static final String PATH_IMAGE_PATH = OUTPUT_PATH + "/graph.png";

    public static void main(String[] args) throws IOException {
        File dir = new File(INPUT_PATH);

        if (!dir.isDirectory())
            throw new FileNotFoundException(INPUT_PATH + " directory not found");

        File outputDir = new File(OUTPUT_PATH);

        if (!outputDir.exists())
            outputDir.mkdirs();

        if (!outputDir.isDirectory())
            throw new FileNotFoundException(OUTPUT_PATH + " directory not found");

        double[][] graphMatrix = FilesUS1718.readMatrix(new File(INPUT_MATRIX_PATH));
        String[] pointNames = FilesUS1718.readPointNames(new File(INPUT_NAMES_PATH));

        List<Integer> signPoints = new ArrayList<>();
        List<Integer> assemblyPoints = new ArrayList<>();

        for (int i = 0; i < pointNames.length; i++) {
            if (pointNames[i].contains("AP")) {
                assemblyPoints.add(i);
            } else {
                signPoints.add(i);
            }
        }

        if (assemblyPoints.isEmpty()) {
            throw new IllegalStateException("No assembly points found in the input names.");
        }

        int[] signPointsArray = signPoints.stream().mapToInt(Integer::intValue).toArray();
        int[] assemblyPointsArray = assemblyPoints.stream().mapToInt(Integer::intValue).toArray();
        PathInfo[] paths = DijkstraMultiAP.dijkstraMultiAP(graphMatrix, signPointsArray, assemblyPointsArray);

        // Escreve os caminhos no arquivo de saída
        writePathsToFile(paths, pointNames);

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

        FileSinkDOT sink = new FileSinkDOT();
        sink.writeAll(graph, GRAPH_PATH);

        String[] command = {"dot", "-Tpng", GRAPH_PATH, "-o", PATH_IMAGE_PATH};
        Runtime.getRuntime().exec(command);
    }

    private static void writePathsToFile(PathInfo[] paths, String[] pointNames) throws IOException {
        File outputCSVFile = new File(OUTPUT_CSV_PATH);
        if (!outputCSVFile.exists()) outputCSVFile.createNewFile();

        if (!outputCSVFile.isFile() || !outputCSVFile.canWrite()) {
            throw new FileNotFoundException(OUTPUT_CSV_PATH + " file not found");
        }

        try (Writer outputCSVFileWriter = new FileWriter(outputCSVFile)) {
            for (PathInfo pathInfo : paths) {
                String namedPath = replaceIndicesWithNames(pathInfo.path, pointNames);
                outputCSVFileWriter.write(namedPath + ". Cost: " + pathInfo.distance + "\n");
            }
        }
    }

    private static String replaceIndicesWithNames(String path, String[] pointNames) {
        StringBuilder namedPath = new StringBuilder();
        String[] indices = path.split(" -> ");
        for (String index : indices) {
            int idx = Integer.parseInt(index.trim());
            if (namedPath.length() > 0) {
                namedPath.append(" -> ");
            }
            namedPath.append(pointNames[idx]);
        }
        return namedPath.toString();
    }
}