package ipp.isep.p1231360;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainUS17 {
    private static final String INPUT_PATH = "src/main/resources/in/us17";
    private static final String OUTPUT_PATH = "src/main/resources/out/us17";
    private static final String INPUT_MATRIX_PATH = INPUT_PATH + "/us17_matrix.csv";
    private static final String INPUT_NAMES_PATH = INPUT_PATH + "/us17_points_names.csv";
    private static final String OUTPUT_CSV_PATH = OUTPUT_PATH + "/shortest_paths.csv";
    private static final String GRAPH_PATH = OUTPUT_PATH + "/graph.gv";
    private static final String GRAPH_IMAGE = OUTPUT_PATH + "/graph.png";
    private static final String INDIVIDUAL_PATH_IMAGES_DIR = OUTPUT_PATH + "/paths";

    public static void main(String[] args) throws IOException {
        File dir = new File(INPUT_PATH);

        if(!dir.isDirectory())
            throw new FileNotFoundException(INPUT_PATH + " directory not found");

        File outputDir = new File(OUTPUT_PATH);

        if (!outputDir.exists())
            outputDir.mkdirs();

        if (!outputDir.isDirectory())
            throw new FileNotFoundException(OUTPUT_PATH + " directory not found");

        double[][] graphMatrix = FilesUS1718.readMatrix(new File(INPUT_MATRIX_PATH));
        String[] pointNames = FilesUS1718.readPointNames(new File(INPUT_NAMES_PATH));

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

        // Calcular o caminho mais curto de um ponto inserido pelo utilizador
        //Quando for a tirar os comentários disto comentar linhas: 79,80, 83,86,89
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the point:");
        String pointName = scanner.nextLine().trim();

        // Validar se o ponto existe no array pointNames
        int pointIndex = -1;
        for (int i = 0; i < pointNames.length; i++) {
            if (pointNames[i].equalsIgnoreCase(pointName)) {
                pointIndex = i;
                break;
            }
        }

        if (pointIndex == -1) {
            System.out.println("Point name not found.");
            return;
        }
        PathInfo[] singlePath = Dijkstra.dijkstra(graphMatrix, new int[]{pointIndex}, assemblyPoint);

        // Caminho mais curto e custo na consola
        PathInfo paths = singlePath[0];
        String namedPath = replaceIndicesWithNames(paths.path, pointNames);
        System.out.println("Path: " + namedPath + ". Cost: " + paths.distance);*/

        int[] signPointsArray = signPoints.stream().mapToInt(Integer::intValue).toArray();
        PathInfo[] paths = Dijkstra.dijkstra(graphMatrix, signPointsArray, assemblyPoint);

        // Encontra o caminho de menor custo entre os pontos de sinalização e o ponto de montagem
        PathInfo shortestPath = findShortestPath(paths);

        // Escreve os caminhos no arquivo de saída
        writePathsToFile(paths, shortestPath, pointNames);

        // Gera imagens para cada caminho individual
        generateIndividualPathImages(paths, pointNames, graphMatrix);
        //Para o ponto pedido na consola
        //generateIndividualPathImages(singlePath, pointNames, graphMatrix);

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

        String[] command = {"dot", "-Tpng", GRAPH_PATH, "-o", GRAPH_IMAGE};
        Runtime.getRuntime().exec(command);
    }

    private static void generateIndividualPathImages(PathInfo[] paths, String[] pointNames, double[][] graphMatrix) throws IOException {
        File individualPathsDir = new File(INDIVIDUAL_PATH_IMAGES_DIR);
        if (!individualPathsDir.exists()) {
            individualPathsDir.mkdirs();
        }

        for (PathInfo path : paths) {
            Graph graph = new SingleGraph("PathGraph");
            String[] pathNodes = path.path.split(" -> ");
            for (String node : pathNodes) {
                int nodeIndex = Integer.parseInt(node.trim());
                graph.addNode(pointNames[nodeIndex]).setAttribute("ui.label", pointNames[nodeIndex]);
            }

            for (int i = 0; i < pathNodes.length - 1; i++) {
                int nodeIndex1 = Integer.parseInt(pathNodes[i].trim());
                int nodeIndex2 = Integer.parseInt(pathNodes[i + 1].trim());
                String edgeId = pointNames[nodeIndex1] + "-" + pointNames[nodeIndex2];
                if (graph.getEdge(edgeId) == null) {
                    graph.addEdge(edgeId, pointNames[nodeIndex1], pointNames[nodeIndex2])
                            .setAttribute("distance", graphMatrix[nodeIndex1][nodeIndex2]);
                }
            }

            String graphPath = INDIVIDUAL_PATH_IMAGES_DIR + "/" + pointNames[Integer.parseInt(pathNodes[0].trim())] + "_to_" + pointNames[Integer.parseInt(pathNodes[pathNodes.length - 1].trim())] + ".gv";
            FileSinkDOT sink = new FileSinkDOT();
            sink.writeAll(graph, graphPath);

            String imagePath = INDIVIDUAL_PATH_IMAGES_DIR + "/" + pointNames[Integer.parseInt(pathNodes[0].trim())] + "_to_" + pointNames[Integer.parseInt(pathNodes[pathNodes.length - 1].trim())] + ".png";
            String[] command = {"dot", "-Tpng", graphPath, "-o", imagePath};
            Runtime.getRuntime().exec(command);
        }
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

    private static void writePathsToFile(PathInfo[] paths, PathInfo shortestPath, String[] pointNames) throws IOException {
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

            // Adiciona a linha extra com o caminho de menor custo
            String namedShortestPath = replaceIndicesWithNames(shortestPath.path, pointNames);
            outputCSVFileWriter.write("\nShortest Path: " + namedShortestPath + ". Cost: " + shortestPath.distance);
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