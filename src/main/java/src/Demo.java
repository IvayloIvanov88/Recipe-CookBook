package src;


import org.jetbrains.annotations.NotNull;
import src.recipe.*;
import src.utils.ConsoleArt;
import src.utils.CsvOperation;
import src.utils.RecipeOperation;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static src.utils.RecipeOperation.*;

public class Demo {

    public static final String PRINT_LINE = "|----------------------------------------------|\n";

    private static final String UNHIDDEN_RECIPE_PATH = "src/main/java/src/recipe.csv";
    private static final String HIDDEN_RECIPE_PATH = "src/main/java/src/hidden.csv";
    private static final String DELIMITER = "delimiter";

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {

        List<Recipe> unhiddenRecipes = new ArrayList<>();
        List<Recipe> hiddenRecipes = new ArrayList<>();

        List<String[]> unhiddenRecipesData = CsvOperation.readFromCSV(UNHIDDEN_RECIPE_PATH);
        List<String[]> hiddenRecipesData = CsvOperation.readFromCSV(HIDDEN_RECIPE_PATH);

        RecipeOperation.addRecipesInList(unhiddenRecipes, unhiddenRecipesData);
        RecipeOperation.addRecipesInList(hiddenRecipes, hiddenRecipesData);

        String defaultRecipesPath = UNHIDDEN_RECIPE_PATH;
        List<Recipe> defaultRecipes = unhiddenRecipes;
        List<String[]> defaultRecipesData = unhiddenRecipesData;
        showOptions();

        String choose = "";
        while (!(choose = SCANNER.nextLine()).trim().equalsIgnoreCase("exit")) {

            switch (choose) {
                case "1":
                    AtomicInteger count = new AtomicInteger(0);
                    defaultRecipes.forEach(r -> System.out.printf("%n%d. %s%n", count.addAndGet(1), r));
                    break;

                case "2":
                    String recipeName = getUserChoose("Enter recipe's name: ");
                    if (!RecipeOperation.isRecipeExist(defaultRecipesData, recipeName)) {
                        String[] filesToAddInCSV = getUsersChooseFileToAdd(recipeName);
                        CsvOperation.writeInCSV(defaultRecipesPath, filesToAddInCSV);
                        RecipeOperation.addOneRecipeInList(defaultRecipes, filesToAddInCSV);
                        System.out.println(ANSI_GREEN + "Recipe was added." + ANSI_RESET);
                    } else {
                        System.err.println("There is already such recipe.");
                    }
                    break;

                case "3":
                    listAllRecipesByName(defaultRecipes);
                    editRecipe(defaultRecipesPath, defaultRecipesData, defaultRecipes);
                    break;
                case "4":
                    listAllRecipesByName(defaultRecipes);
                    choose = getUserChoose("Choose number to delete.");
                    try {
                        int userChoose = Integer.parseInt(choose);
                        CsvOperation.deleteFromCSV(defaultRecipesPath, userChoose);
                        defaultRecipes.remove(userChoose - 1);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.err.println("Try with valid number!");
                    }

                    break;
                case "5":
                    listAllRecipesByName(defaultRecipes);
                    choose = getUserChoose("Enter recipe`s name. ");
                    getRecipeByName(defaultRecipes, choose).forEach(System.out::println);
                    break;

                case "6":
                    choose = getUserChoose("Enter recipe`s type\nChoose one : Meet, meatless, desert, salad, alaminut, pasta, soup.");
                    getRecipeByFilter(defaultRecipes, choose).forEach(System.out::println);
                    break;

                case "123":
                    choose = getUserChoose("How old are you, and don't lie ?");

                    if (validateUserAge(choose)) {
                        System.out.println(ANSI_GREEN + "Welcome to the secret section with alcoholic beverages.\n" +
                                "to go back, just type 'back' " + ANSI_RESET);
                    } else {
                        System.err.println("You are still very young!");
                        break;
                    }
                    defaultRecipesPath = HIDDEN_RECIPE_PATH;
                    defaultRecipes = hiddenRecipes;
                    defaultRecipesData = hiddenRecipesData;

                    break;
                case "back":
                    System.out.println(ANSI_GREEN +"You are back to normal, unhidden recipes."+ ANSI_RESET);
                    defaultRecipesPath = UNHIDDEN_RECIPE_PATH;
                    defaultRecipes = unhiddenRecipes;
                    defaultRecipesData = unhiddenRecipesData;
                    break;
                default:
                    System.out.println(ANSI_GREEN + "Try again, read carefully options." + ANSI_RESET);

            }
            showOptions();
        }
        ConsoleArt artGen = new ConsoleArt();
        artGen.draw("Goodbye", 13, ANSI_GREEN + "$" + ANSI_RESET);
        artGen.draw("Bon Appetit", 13, ANSI_RED + "#" + ANSI_RESET);

    }


    private static String getUserChoose(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
        return Demo.SCANNER.nextLine();
    }

    private static void editRecipe(String path, List<String[]> fileData, List<Recipe> recipes) {

        String recipeName = getUserChoose("Choose recipe by name to change.");

        if (RecipeOperation.isRecipeExist(fileData, recipeName) ||
                RecipeOperation.isRecipeContainsRecipeWithSameName(recipes, recipeName)) {

            List<Recipe> recipeByName = getRecipeByName(recipes, recipeName);
            recipes.removeAll(recipeByName);
            String[] currentRecipe = fileData.stream().filter(r -> recipeName.equals(r[0])).findAny().orElse(null);
            int idx = fileData.indexOf(currentRecipe) + 1;
            CsvOperation.deleteFromCSV(path, idx);

            String[] usersChooseFileToAdd = getUsersChooseFileToAdd(recipeName);
            CsvOperation.writeInCSV(path, usersChooseFileToAdd);
            RecipeOperation.addOneRecipeInList(recipes, usersChooseFileToAdd);
            System.out.println(ANSI_GREEN + "Recipe edited successfully." + ANSI_RESET);
        } else {
            System.err.println("There is no such recipe.");
        }
    }

    @NotNull
    private static String[] getUsersChooseFileToAdd(String recipeName) {
        StringBuilder sb = new StringBuilder();

        sb.append(recipeName).append(DELIMITER);

        System.out.println("How many portions ?");
        String toAppendYield = Demo.SCANNER.nextLine();
        sb.append(toAppendYield).append(DELIMITER);

        System.out.println("Preparation time ?");
        String toAppendPreparationTime = Demo.SCANNER.nextLine();
        sb.append(toAppendPreparationTime).append(DELIMITER);

        System.out.println("What are ingredients ?");
        String toAppendIngredients = Demo.SCANNER.nextLine();
        sb.append(toAppendIngredients).append(DELIMITER);

        System.out.println("What are the preparation steps ?");

        String toAppendThirdStep = Demo.SCANNER.nextLine();
        sb.append(toAppendThirdStep).append(DELIMITER);

        return sb.toString().split(DELIMITER);

    }

    public static void showOptions() {
        System.out.println(PRINT_LINE +
                "|\t\t" + ANSI_RED + "Welcome to Experian`s recipe book !" + ANSI_RESET + "    |\n" +
                PRINT_LINE +
                "|\t1. Read all recipes                        |\n" +
                PRINT_LINE +
                "|\t2. Add a recipe                            |\n" +
                PRINT_LINE +
                "|\t3. Edit recipe                             |\n" +
                PRINT_LINE +
                "|\t4. Delete recipe                           |\n" +
                PRINT_LINE +
                "|\t5. Find specific recipe by name            |\n" +
                PRINT_LINE +
                "|\t6. Find specific recipe by type            |\n" +
                PRINT_LINE + PRINT_LINE +
                "|\tTo exit type: exit                         |\n" +
                PRINT_LINE);


    }

    public static boolean validateUserAge(String userChoose) {

        try {
            int age = Integer.parseInt(userChoose);
            return age >= 18;
        } catch (NumberFormatException e) {
            System.err.println("Enter age in digits.");
        }
        return false;
    }
}