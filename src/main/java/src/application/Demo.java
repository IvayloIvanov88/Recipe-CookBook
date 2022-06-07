package src.application;


import org.w3c.dom.ls.LSOutput;
import src.recipe.*;
import src.services.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static src.services.RecipeService.*;
import static src.services.RecipeService.getRecipeByName;
import static src.services.UserService.SCANNER;

public class Demo {


    private static final String UNHIDDEN_RECIPE_PATH = "src/main/java/src/recipe.csv";
    private static final String HIDDEN_RECIPE_PATH = "src/main/java/src/hidden.csv";

    public static void main(String[] args) {

        List<Recipe> unhiddenRecipes = new ArrayList<>();
        List<Recipe> hiddenRecipes = new ArrayList<>();

        List<String[]> unhiddenRecipesData = CSVFileService.readFromCSV(UNHIDDEN_RECIPE_PATH);
        List<String[]> hiddenRecipesData = CSVFileService.readFromCSV(HIDDEN_RECIPE_PATH);

        RecipeService.addRecipesInList(unhiddenRecipes, unhiddenRecipesData);
        RecipeService.addRecipesInList(hiddenRecipes, hiddenRecipesData);

        String defaultRecipesPath = UNHIDDEN_RECIPE_PATH;
        List<Recipe> defaultRecipes = unhiddenRecipes;
        List<String[]> defaultRecipesData = unhiddenRecipesData;

        MenuService.showOptions();

        String choose = "";
        while (!(choose = SCANNER.nextLine()).trim().equalsIgnoreCase("exit")) {

            switch (choose) {
                case "1":
                    AtomicInteger count = new AtomicInteger(0);
                    defaultRecipes.forEach(r -> System.out.printf("%n%d. %s%n", count.addAndGet(1), r));
                    break;

                case "2":
                    String recipeName = UserService.getUserChoose("Enter recipe's name: ");
                    if (!RecipeService.isRecipeExist(defaultRecipesData, recipeName)) {
                        String[] filesToAddInCSV = UserService.getUsersChooseFileToAdd(recipeName, 0, 0);
                        CSVFileService.writeInCSV(defaultRecipesPath, filesToAddInCSV);

                        List<String[]> filesToAddInList = new ArrayList<>(Collections.singleton(filesToAddInCSV));
                        RecipeService.addRecipesInList(defaultRecipes, filesToAddInList);
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
                    choose = UserService.getUserChoose("Choose number to delete.");
                    int userChoose = Integer.parseInt(choose);
                    try {
                        CSVFileService.deleteFromCSV(defaultRecipesPath, userChoose);
                        defaultRecipes.remove(userChoose - 1);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.err.println("Try with valid number!");
                    }

                    break;
                case "5":
                    listAllRecipesByName(defaultRecipes);
                    choose = UserService.getUserChoose("Enter recipe`s name.");
                    List<Recipe> recipeByName = getRecipeByName(defaultRecipes, choose);

                    if (recipeByName.isEmpty()) {
                        System.out.println("Wrong recipe name or missing such recipe ");
                        break;
                    }
                    recipeByName.forEach(System.out::println);
                    RecipeService.evaluateRecipe(defaultRecipes, choose);
                    break;
                case "6":
                    choose = UserService.getUserChoose("Enter recipe`s type\nChoose one : Meet, meatless, desert, salad, alaminut, pasta, soup.");
                    getRecipeByFilter(defaultRecipes, choose).forEach(System.out::println);
                    break;

                case "123":
                    choose = UserService.getUserChoose("How old are you, and don't lie ?");

                    if (UserService.validateUserAge(choose)) {
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
            MenuService.pressEnterToContinue();
            MenuService.showOptions();
        }
        ConsoleArtService artGen = new ConsoleArtService();
        artGen.draw("Goodbye", 13, ANSI_GREEN + "$" + ANSI_RESET);
        artGen.draw("Bon Appetit", 13, ANSI_RED + "#" + ANSI_RESET);
    }

}