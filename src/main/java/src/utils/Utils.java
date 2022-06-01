package src.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.jetbrains.annotations.NotNull;
import src.recipe.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Utils {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    static Scanner scanner = new Scanner(System.in);

    private Utils() {
    }

    public static void deleteRecord(String pathName, int index) {
        CSVReader reader ;
        try {
            reader = new CSVReader(new FileReader(pathName));

            List<String[]> allElements = reader.readAll();
            allElements.remove(index);
            FileWriter fileWriter = new FileWriter(pathName);
            CSVWriter writer = new CSVWriter(fileWriter);
            writer.writeAll(allElements);
            writer.close();
        } catch (IOException e) {
            System.err.println("File not found");
        } catch (IndexOutOfBoundsException e) {
            System.err.println(" Index out of bounds");
        }
    }

    public static void writeDataAtOnce(String pathName, String[] files) {
        File file = new File(pathName);

        try {
            FileWriter outputFile = new FileWriter(file, true);

            CSVWriter writer = new CSVWriter(outputFile);

            List<String[]> data = new ArrayList<>();
            data.add(files);

            List<String[]> oldData = readingCSV(pathName);
            if (!(oldData.contains(files))) {
                writer.writeAll(data);
                // todo трие ми рецептата
            }


            writer.close();
        } catch (IOException e) {
            System.err.println("File not found");
        }
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
            recipe = getRecipeType(recipeName);

            recipe.setName(nextLine[0]);

            try {
                recipe.setYield(Integer.parseInt(nextLine[1].trim()));
                recipe.setPrepTime(Integer.parseInt(nextLine[2].trim()));
            } catch (NumberFormatException e) {
                System.err.println("Enter digits for yield and for preparation time");
            }
            recipe.addAllIngredient(Arrays.stream(nextLine[3].split(",")).collect(Collectors.toList()));

//            AtomicInteger countSteps = new AtomicInteger(0);
            int countSteps = 0;
            String[] split = nextLine[4].split("\n");
            for (int i = 0; i < split.length; i++) {
                recipe.setDirections(countSteps++, split[i]);
            }

            recipes.add(recipe);

            for (int i = 1; i < recipes.size(); i++) {
                if (recipes.get(i - 1).getName().equals(recipeName)) {
                    recipes.remove(recipe);
                }
            }
        }
    }


    public static boolean addOneRecipeInList(List<Recipe> recipes, String[] data) {
        Recipe recipe;

        String recipeName = data[0];
        recipe = getRecipeType(recipeName);

        if (recipes.stream().anyMatch(r -> r.getName().equals(recipeName))){
            return false;
        }else {
            recipe.setName(data[0]);
            try {
                recipe.setYield(Integer.parseInt(data[1]));
                recipe.setPrepTime(Integer.parseInt(data[2]));
            } catch (NumberFormatException e) {
                System.err.println("Enter digit for yield and for preparation time");
            }
            recipe.addAllIngredient(Arrays.stream(data[3].split(",")).collect(Collectors.toList()));

            AtomicInteger countSteps = new AtomicInteger(0);
            String[] split = data[4].split("\\.");
            for (int i = 0; i < split.length; i++) {
                recipe.setDirections(countSteps.addAndGet(1), split[i]);
            }

            recipes.add(recipe);
            return true;
        }


//        for (int i = 1; i < recipes.size(); i++) {
//            if (recipes.get(i - 1).getName().equals(recipeName)) {
//                recipes.remove(recipe);
//            }
//        }
    }


    @NotNull
    private static Recipe getRecipeType(String name) {

        Recipe recipe = null;
        boolean again = false;
        String[] fullName = name.split("\\s+");
        int count = 0;
        String firstWordOfName;


        for (int i = 0; i <= fullName.length; i++) {
            if (again) {
                System.out.println("Specified recipe type:\n Meat, Meatless, Soup, Salad, Desert, Pasta or Alaminut");
                firstWordOfName = scanner.nextLine();
            } else {
                firstWordOfName = fullName[i];
            }
            switch (firstWordOfName.toLowerCase().trim()) {
                case "meatrecipe":
                case "meat":
                case "pork":
                case "chicken":
                    recipe = new MeatRecipe();
                    return recipe;
                case "meatlessrecipe":
                case "meatless":
                case "no meat":
                case "vegan":
                case "without meat":
                    recipe = new MeatlessRecipe();
                    return recipe;
                case "saladrecipe":
                case "salad":
                    recipe = new SaladRecipe();
                    return recipe;
                case "souprecipe":
                case "soup":
                    recipe = new SoupRecipe();
                    return recipe;
                case "desertrecipe":
                case "desert":
                case "sweet":
                    recipe = new DesertRecipe();
                    return recipe;
                case "alaminutrecipe":
                case "alaminut":
                    recipe = new AlaminutRecipe();
                    return recipe;
                case "pastarecipe":
                case "bread":
                case "pasta":
                case "dough":
                case "pancakes":
                    recipe = new PastaRecipe();
                    return recipe;
                case "cocktailrecipe":
                case "cocktail":
                case "drinks":
                    recipe = new CocktailRecipe();
                    return recipe;
                default:
                    count++;
                    again = count > fullName.length - 1;
            }
        }
        System.out.println("Specified recipe type:\n Meat, Meatless, Soup, Salad, Desert, Pasta or Alaminut");
        firstWordOfName = scanner.nextLine();
        recipe = getRecipeType(firstWordOfName);
        return recipe;
    }

}
