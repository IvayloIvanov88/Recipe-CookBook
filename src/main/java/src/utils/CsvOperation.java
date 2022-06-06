package src.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvOperation {

    public static List<String[]> readFromCSV(String fileName) {
        List<String[]> allData = null;
        try {
            FileReader filereader = new FileReader(fileName);
            try (CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build()) {
                allData = csvReader.readAll();
            }
            filereader.close();
        } catch (IOException e) {
            System.err.println("File not found.");
        }
        return allData;
    }

    public static void writeInCSV(String pathName, String[] files) {
        File file = new File(pathName);

        try {
            FileWriter outputFile = new FileWriter(file, true);

            try (CSVWriter writer = new CSVWriter(outputFile)) {

                List<String[]> data = new ArrayList<>();
                data.add(files);
                writer.writeAll(data);
            }
        } catch (IOException e) {
            System.err.println("File not found");
        }
    }

    public static void deleteFromCSV(String pathName, int index) {

        try {
            List<String[]> allElements;
            try (CSVReader reader = new CSVReader(new FileReader(pathName))) {

                allElements = reader.readAll();
            }
            allElements.remove(index);
            try (FileWriter fileWriter = new FileWriter(pathName)) {
                try (CSVWriter writer = new CSVWriter(fileWriter)) {
                    writer.writeAll(allElements);
                }
            }
        } catch (IOException e) {
            System.err.println("File not found");
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Index out of bounds");
        }
    }

}
