package ipp.isep.p1231360;

import java.io.*;
import java.util.Collections;
import java.util.Scanner;

public class MainUS14 {
    private static final String INPUT_PATH = "src/main/resources/in/us14";
    private static final String OUTPUT_PATH = "src/main/resources/out/us14";
    private static final String OUTPUT_CSV_PATH = OUTPUT_PATH + "/us14.csv";
    private static final String GNUPLOT_TEMPLATE_PATH = "src/main/resources/gnuplot_chart.p";
    private static final int REPETITION_COUNT = 10;

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

                if(!pathname.getName().startsWith("fx_2feira_"))
                    return false;

                if(!pathname.getName().endsWith(".csv"))
                    return false;

                return true;
            }
        });

        if (files == null)
            return;

        double totalSum = 0;

        File outputFile = new File(OUTPUT_CSV_PATH);

        if (!outputFile.exists())
            outputFile.createNewFile();

        if (!outputFile.isFile())
            throw new FileNotFoundException(OUTPUT_CSV_PATH + " file not found");

        FileWriter fileOut = new FileWriter(outputFile);

        for (File file : files) {
            Graph graph = new Graph();

            System.out.printf("File: %s%n", file.getName());

            double sum = 0;
            int cost = 0;

            for (int i = 0; i < REPETITION_COUNT; i++) {
                graph = new Graph();

                Scanner fileIn = new Scanner(file);

                Files.readFile(fileIn, graph);

                fileIn.close();

                long startTime = System.nanoTime();

                Collections.sort(graph.edgeList);

                cost = Kruskal.kruskalCost(graph);

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

            fileOut.append(String.format("%d;%f%n", graph.edgeList.size(), sum / REPETITION_COUNT * 1e-6));
        }

        fileOut.close();

        System.out.printf("%n%nTotal average execution time: %f ms%n", totalSum / files.length * 1e-6);

        String[] command = { "gnuplot", GNUPLOT_TEMPLATE_PATH };
        Runtime.getRuntime().exec(command);
    }
}