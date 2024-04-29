package lpschool;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static final String SRC_DIR_PATH = "src/main/resources";
    private static final int REPETITION_COUNT = 1;

    public static void main(String[] args) throws FileNotFoundException {
        File dir = new File(SRC_DIR_PATH);

        if(!dir.isDirectory())
            throw new FileNotFoundException(SRC_DIR_PATH + " directory not found");

        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if(!pathname.isFile())
                    return false;

                if(!pathname.getName().endsWith(".csv"))
                    return false;

                return true;
            }
        });

        if (files == null)
            return;

        double totalSum = 0;

        for (File file : files) {
            Graph graph = new Graph();

            System.out.printf("File: %s%n", file.getName());

            double sum = 0;
            int cost = 0;

            for (int i = 0; i < REPETITION_COUNT; i++) {
                graph = new Graph();

                Scanner fileIn = new Scanner(file);

                readFile(fileIn, graph);

                fileIn.close();

                Collections.sort(graph.edgeList);

                long startTime = System.nanoTime();

                cost = Kruskal.kruskal(graph);

                long endTime = System.nanoTime();

                sum += endTime - startTime;
            }

            System.out.printf(
                    "Graph Dimension = %d : Graph Order = %d : Minimum cost = %d%n",
                    graph.edgeList.size(),
                    graph.vertexList.size(),
                    cost
            );

            totalSum += sum / REPETITION_COUNT;

            System.out.printf("Average execution time: %f ms%n", sum / REPETITION_COUNT * 1e-6);
        }

        System.out.printf("%n%nTotal average execution time: %f ms%n", totalSum / files.length * 1e-6);
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
}