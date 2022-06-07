package src;


import org.jetbrains.annotations.NotNull;
import src.recipe.*;
import src.utils.ConsoleArt;
import src.utils.CsvOperation;
import src.utils.RecipeOperation;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static src.utils.Menu.showOptions;
import static src.utils.RecipeOperation.*;
import static src.utils.RecipeOperation.getRecipeByName;

public class Demo {


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
                        String[] filesToAddInCSV = getUsersChooseFileToAdd(recipeName, 0, 0);
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
                    choose = getUserChoose("Enter recipe`s name.");
                    getRecipeByName(defaultRecipes, choose).forEach(System.out::println);
                    if (getRecipeByName(defaultRecipes, choose).isEmpty())
                        break;
                    int userRating = Integer.parseInt(getUserChoose("Evaluate the recipe."));
                    List<Recipe> recipesToEvaluate = getRecipeByName(defaultRecipes, choose);
                    recipesToEvaluate.get(0).setUserRating(userRating);
                    // todo трябва да запише промените и във файла


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
                    System.out.println(ANSI_GREEN + "You are back to normal, unhidden recipes." + ANSI_RESET);
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


    public static String getUserChoose(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
        return Demo.SCANNER.nextLine();
    }

    @NotNull
    public static String[] getUsersChooseFileToAdd(String recipeName, int voteCount, double rating) {
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

        if (rating == 0 && voteCount == 0) {
            String toAppendDefaultRating = "0";
            sb.append(toAppendDefaultRating).append(DELIMITER);

            String toAppendDefaultVoteCount = "0";
            sb.append(toAppendDefaultVoteCount).append(DELIMITER);
        } else {
            String toAppendRating = String.valueOf(rating);
            sb.append(toAppendRating).append(DELIMITER);

            String toAppendVoteCount = String.valueOf(voteCount);
            sb.append(toAppendVoteCount).append(DELIMITER);
        }
        return sb.toString().split(DELIMITER);

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