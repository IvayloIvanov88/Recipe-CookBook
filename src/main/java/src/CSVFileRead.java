package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFileRead {
    static Scanner scanner;

    public static List<String> readCSV(String path) {
        List<String> line = new ArrayList<>();

        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Enter valid file path");
        }
//        sc.useDelimiter(",");
        while (scanner.hasNext()) {
        line.add(scanner.next());
        }
        scanner.close();
        return line;
    }
}
