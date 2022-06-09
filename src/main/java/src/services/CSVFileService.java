package src.services;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFileService {
    private CSVFileService() {
    }

    private static final String FILE_NOT_FOUND = "File not found.";

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
            System.err.println(FILE_NOT_FOUND);
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
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println(FILE_NOT_FOUND);
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
                    writer.flush();
                }
            }
        } catch (IOException e) {
            System.err.println(FILE_NOT_FOUND);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Index out of bounds");
        }
    }


    public static void updateCSV(String fileToUpdate, String updateData, String oldData, String recipeName) {
        File inputFile = new File(fileToUpdate);

        String oldDataWithoutZero = oldData.replaceAll(".0+", "");

        List<String[]> csvBody = null;
        try (CSVReader reader = new CSVReader(new FileReader(inputFile))) {
            csvBody = reader.readAll();

            for (String[] strArray : csvBody) {
                String currentRecipeName = strArray[0];
                for (int j = strArray.length - 1; j >= 0; j--) {
                    if (currentRecipeName.equalsIgnoreCase(recipeName)) {
                        if (strArray[j].equalsIgnoreCase(oldDataWithoutZero)) { //String to be replaced
                            strArray[j] = updateData;//Target replacement
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(FILE_NOT_FOUND);

        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(inputFile))) {
            writer.writeAll(csvBody);
            writer.flush();
        } catch (IOException e) {
            System.err.println(FILE_NOT_FOUND);
        }
    }
}
