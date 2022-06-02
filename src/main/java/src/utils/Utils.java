package src.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import src.recipe.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Utils {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    static Scanner scanner = new Scanner(System.in);

    private Utils() {
    }

    public static void deleteData(String pathName, int index) {

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
            System.err.println(" Index out of bounds");
        }
    }

    public static void writeData(String pathName, String[] files) {
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
    public static boolean isRecipeExist(List<String[]> fileData, String title) {
        //removed for loop to avoid double iteration when using get() method
        return fileData.stream().anyMatch((r -> r[0].equals(title)));
    }

    public static boolean isRecipeContainsRecipeWithSameName(List<Recipe> recipes, String recipeName) {
        return recipes.stream().map(Recipe::getName).anyMatch(recipeName::equalsIgnoreCase);
    }

    public static List<String[]> readingCSV(String fileName) {
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

    public static void creatingRecipeList(List<Recipe> recipes, List<String[]> allData) {
        Recipe recipe;
        String[] nextLine;
        for (String[] row : allData) {
            nextLine = row;

            String recipeName = nextLine[0];

            if (!isRecipeContainsRecipeWithSameName(recipes, recipeName)) {
                recipe = getRecipeType(recipeName);

                recipe.setName(nextLine[0]);

                try {
                    recipe.setServing(Integer.parseInt(nextLine[1].trim()));
                    recipe.setPrepTime(Integer.parseInt(nextLine[2].trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Enter digits for serving and for preparation time");
                }

                recipe.addAllIngredient(Arrays.stream(nextLine[3].split(",")).collect(Collectors.toList()));

                int countSteps = 1;
                String[] split = nextLine[4].split("\n");
                for (int i = 0; i < split.length; i++) {
                    recipe.setDirections(countSteps++, split[i]);
                }

                recipes.add(recipe);
            }
        }
    }


    public static void addOneRecipeInList(List<Recipe> recipes, String[] data) {
        Recipe recipe;

        String recipeName = data[0];

        if (!isRecipeContainsRecipeWithSameName(recipes, recipeName)) {

            recipe = getRecipeType(recipeName);

            recipe.setName(data[0]);
            try {
                recipe.setServing(Integer.parseInt(data[1]));
                recipe.setPrepTime(Integer.parseInt(data[2]));
            } catch (NumberFormatException e) {
                System.err.println("Enter digit for yield and for preparation time");
            }
            recipe.addAllIngredient(Arrays.stream(data[3].split(",")).collect(Collectors.toList()));

            int countSteps = 1;
            String[] split = data[4].split("\\.");
            for (int i = 0; i < split.length; i++) {
                recipe.setDirections(countSteps++, split[i]);
            }

            recipes.add(recipe);
        }
    }


    private static Recipe getRecipeType(String name) {

        Recipe recipe;
        String[] fullName = name.split("\\s+");
        String firstWordOfName;

        for (int i = 0; i < fullName.length; i++) {
            firstWordOfName = fullName[i];

            switch (firstWordOfName.toLowerCase().trim()) {
                case "meat recipe":
                case "meat":
                case "pork":
                case "chicken":
                    recipe = new MeatRecipe();
                    return recipe;
                case "meatless recipe":
                case "meatless":
                case "no meat":
                case "vegan":
                case "without meat":
                    recipe = new MeatlessRecipe();
                    return recipe;
                case "salad recipe":
                case "salad":
                    recipe = new SaladRecipe();
                    return recipe;
                case "soup recipe":
                case "soup":
                    recipe = new SoupRecipe();
                    return recipe;
                case "dessert recipe":
                case "dessert":
                case "sweet":
                    recipe = new DessertRecipe();
                    return recipe;
                case "alaminut recipe":
                case "alaminut":
                    recipe = new AlaminutRecipe();
                    return recipe;
                case "pasta recipe":
                case "bread":
                case "pasta":
                case "dough":
                case "pancakes":
                    recipe = new PastaRecipe();
                    return recipe;
                case "cocktail recipe":
                case "cocktail":
                case "drinks":
                    recipe = new CocktailRecipe();
                    return recipe;
                default:

            }
        }
        System.out.println("Specified recipe type:\n Meat, Meatless, Soup, Salad, Desert, Pasta or Alaminut");
        firstWordOfName = scanner.nextLine();
        recipe = getRecipeType(firstWordOfName);
        return recipe;
    }

}
