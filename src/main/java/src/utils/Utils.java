package src.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import src.recipe.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private Utils() {};

    public static void stopTheSystem(String message) {
        System.err.println(message);
        System.exit(-1);
    }

    public static void deleteRecord(String pathName, int index){
        CSVReader reader2 = null;
        try {
            reader2 = new CSVReader(new FileReader(pathName));

            List<String[]> allElements = reader2.readAll();
            allElements.remove(index);
            FileWriter sw = new FileWriter(pathName);
            CSVWriter writer = new CSVWriter(sw);
            writer.writeAll(allElements);
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("File not found");
        }
    }

    public static void writeDataAtOnce(String pathName, String[] files) {
        File file = new File(pathName);

        try {
            FileWriter outputFile = new FileWriter(file, true);

            CSVWriter writer = new CSVWriter(outputFile);

            List<String[]> data = new ArrayList<>();
            data.add(files);
            writer.writeAll(data);

            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("File not found");
        }
    }

    public static List<String[]> readingCSV(String fileName) {
        List<String[]> allData;
        try {
            FileReader filereader = new FileReader(fileName);
            try (CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build()) {
                allData = csvReader.readAll();
            }
            filereader.close();
        } catch (IOException e) {
            throw new IllegalStateException("File not found.");
        }
        return allData;
    }
    public static void creatingRecipeList(List<Recipe> recipes, List<String[]> allData, String name) {
        Recipe recipe;
        String[] nextLine;

        for (String[] row : allData) {
            nextLine = row;

            recipe = getRecipe(name);

            recipe.setName(nextLine[0]);
            recipe.setYield(Integer.parseInt(nextLine[1]));
            recipe.setPrepTime(Integer.parseInt(nextLine[2]));
            recipe.addAllIngredient(Arrays.stream(nextLine[3].split(",")).collect(Collectors.toList()));
            recipe.setDirections(1, nextLine[4]);
            recipe.setDirections(2, nextLine[5]);
            recipe.setDirections(3, nextLine[6]);

            recipes.add(recipe);
        }
    }


    @NotNull
    private static Recipe getRecipe(String name) {
        Recipe recipe;
        switch (name.toLowerCase().trim()) {
            case "meatrecipe":
            case "meat":
                recipe = new MeatRecipe();
                break;
            case "meatlessrecipe":
            case "meatless":
                recipe = new MeatlessRecipe();
                break;
            case "saladrecipe":
            case "salad":
                recipe = new SaladRecipe();
                break;
            case "souprecipe":
            case "soup":
                recipe = new SoupRecipe();
                break;
            case "desertrecipe":
            case "desert":
                recipe = new DesertRecipe();
                break;
            case "alaminutrecipe":
            case "alaminut":
                recipe = new AlaminutRecipe();
                break;
            case "pastarecipe":
            case "pasta":
                recipe = new PastaRecipe();
                break;
            default:
                throw new IllegalStateException("Wrong recipe type");
        }
        return recipe;
    }
}
