package ipp.isep.p1231360;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FilesUS1718 {
    // Ler a matriz no ficheiro de input
    public static double[][] readMatrix(File file) throws FileNotFoundException {
        int size = 0;
        Scanner lineCounter = new Scanner(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        while (lineCounter.hasNextLine()) {
            lineCounter.nextLine();
            size++;
        }
        lineCounter.close();

        double[][] matrix = new double[size][size];
        Scanner scanner = new Scanner(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        int i = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replace("\uFEFF", ""); // Remove BOM if present
            String[] data = line.split(";");
            for (int j = 0; j < data.length; j++) {
                matrix[i][j] = Double.parseDouble(data[j].trim());
            }
            i++;
        }
        scanner.close();

        return matrix;
    }

    // Ler o ficheiro com os nomes dos pontos
    public static String[] readPointNames(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String line = scanner.nextLine().replace("\uFEFF", ""); // Remove BOM if present
        String[] pointNames = line.split(";");
        scanner.close();
        return pointNames;
    }
}
