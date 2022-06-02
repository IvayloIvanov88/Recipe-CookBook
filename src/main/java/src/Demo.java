package src;


import org.jetbrains.annotations.NotNull;
import src.recipe.*;
import src.utils.ConsoleArt;
import src.utils.Utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static src.utils.Utils.*;

public class Demo {

    public static final String PRINT_LINE = "|----------------------------------------------|\n";

    private static final String UNHIDDEN_RECIPE_PATH = "src/main/java/src/recipe.csv";
    private static final String HIDDEN_RECIPE_PATH = "src/main/java/src/hidden.csv";
    private static final String DELIMITER = "delimiter";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Recipe> unhiddenRecipes = new ArrayList<>();
        List<Recipe> hiddenRecipes = new ArrayList<>();

        List<String[]> unhiddenRecipeData = Utils.readingCSV(UNHIDDEN_RECIPE_PATH);
        List<String[]> hiddenRecipeData = Utils.readingCSV(HIDDEN_RECIPE_PATH);

        Utils.creatingRecipeList(unhiddenRecipes, unhiddenRecipeData);
        Utils.creatingRecipeList(hiddenRecipes, hiddenRecipeData);

        showOptions();

        String choose = "";
        while (!(choose = scanner.nextLine()).trim().equalsIgnoreCase("exit")) {

            switch (choose) {
                case "1":
                    AtomicInteger count = new AtomicInteger(0);
                    unhiddenRecipes.forEach(r -> System.out.printf("%n%d. %s%n", count.addAndGet(1), r));
                    break;

                case "2":
                    String recipeName = getUserChoose(scanner, "Enter recipe's name: ");
                    //method should have one responsibility
                    if (!Utils.isRecipeExist(unhiddenRecipeData, recipeName)) {
                        String[] filesToAdd = getUsersChooseFileToAdd(scanner, recipeName);
                        Utils.writeData(UNHIDDEN_RECIPE_PATH, filesToAdd);
                        Utils.addOneRecipeInList(unhiddenRecipes, filesToAdd);
                        System.out.println(ANSI_GREEN + "Recipe was added." + ANSI_RESET);
                    } else {
                        System.err.println("There is already such recipe");
                    }
                    break;

                case "3":
                    listAllRecipesByName(unhiddenRecipes);
                    editRecipe(UNHIDDEN_RECIPE_PATH, unhiddenRecipeData, unhiddenRecipes, scanner);
                    break;
                case "4":
                    listAllRecipesByName(unhiddenRecipes);
                    choose = getUserChoose(scanner, "Choose number to delete");
                    try {
                        int userChoose = Integer.parseInt(choose);
                        Utils.deleteData(UNHIDDEN_RECIPE_PATH, userChoose);
                        unhiddenRecipes.remove(userChoose - 1);
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.err.println("Try with valid number");
                    }

                    break;
                case "5":
                    listAllRecipesByName(unhiddenRecipes);
                    choose = getUserChoose(scanner, "Enter recipe`s name. ");
                    getRecipeByName(unhiddenRecipes, choose).forEach(System.out::println);
                    break;

                case "6":
                    choose = getUserChoose(scanner, "Enter recipe`s type\nChoose one : Meet, meatless, desert, salad, alaminut, pasta, soup or cocktail");
                    getRecipeByFilter(unhiddenRecipes, choose).forEach(System.out::println);
                    break;

                case "123":
                    choose = getUserChoose(scanner, "How old are you, and don't lie ?");

                    if (validateUserAge(choose)) {
                        System.out.println(ANSI_GREEN + "Welcome to the secret section with alcoholic beverages\n" + ANSI_RESET);
                        hiddenRecipes.forEach(System.out::println);
                    } else {
                        System.err.println("You are still very young");
                    }
                    break;
                default:
                    System.err.println("Try again, read carefully options. ");

            }
            showOptions();
        }
        ConsoleArt artGen = new ConsoleArt();
        artGen.draw("Good bye", 13, ANSI_GREEN + "#" + ANSI_RESET);
        artGen.draw("Bon Appetit", 13, ANSI_RED + "#");

    }


    private static void listAllRecipesByName(List<Recipe> unhiddenRecipes) {
        AtomicInteger countRecipe = new AtomicInteger(0);
        unhiddenRecipes.forEach(r -> System.out.printf("%d. %s%n", countRecipe.addAndGet(1), r.getName()));
    }

    private static String getUserChoose(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    private static void editRecipe(String path, List<String[]> fileData, List<Recipe> recipes, Scanner scanner) {

        String recipeName = getUserChoose(scanner, "Choose recipe by name to change");

        if (Utils.isRecipeExist(fileData, recipeName) ||
                Utils.isRecipeContainsRecipeWithSameName(recipes, recipeName)) {

            List<Recipe> recipeByName = getRecipeByName(recipes, recipeName);
            recipes.removeAll(recipeByName);
            String[] currentRecipe = fileData.stream().filter(r -> recipeName.equals(r[0])).findAny().orElse(null);
            int idx = fileData.indexOf(currentRecipe) + 1;
            Utils.deleteData(path, idx);

            String[] usersChooseFileToAdd = getUsersChooseFileToAdd(scanner, recipeName);
            Utils.writeData(path, usersChooseFileToAdd);
            Utils.addOneRecipeInList(recipes, usersChooseFileToAdd);
            System.out.println(ANSI_GREEN + "Recipe edited successfully." + ANSI_RESET);
        } else {
            System.err.println("There is no such recipe");
        }
    }

    @NotNull
    private static String[] getUsersChooseFileToAdd(Scanner scanner, String recipeName) {
        StringBuilder sb = new StringBuilder();

        System.out.println("Enter recipe`s name.");
        sb.append(recipeName).append(DELIMITER);

        System.out.println("How many portions ?");
        String toAppendYield = scanner.nextLine();
        sb.append(toAppendYield).append(DELIMITER);

        System.out.println("Preparation time ?");
        String toAppendPreparationTime = scanner.nextLine();
        sb.append(toAppendPreparationTime).append(DELIMITER);

        System.out.println("What are ingredients ?");
        String toAppendIngredients = scanner.nextLine();
        sb.append(toAppendIngredients).append(DELIMITER);

        System.out.println("What are the preparation steps ?");

        String toAppendThirdStep = scanner.nextLine();
        sb.append(toAppendThirdStep).append(DELIMITER);

        return sb.toString().split(DELIMITER);

    }


    private static List<Recipe> getRecipeByName(List<Recipe> recipes, String filter) {
        return recipes.stream()
                .filter(r -> Objects.equals(r.getName().toLowerCase(), filter.toLowerCase()))
                .collect(Collectors.toList());
    }

    private static List<Recipe> getRecipeByFilter(List<Recipe> recipes, String filter) {

        return recipes.stream()
                .filter(r -> Objects.equals(r.getClass().getSimpleName().toLowerCase(), (filter + "recipe").toLowerCase()))
                .collect(Collectors.toList());
    }

    public static void showOptions() {
        System.out.println(PRINT_LINE +
                "|\t\t" + ANSI_RED + "Welcome to Experian`s recipe book" + ANSI_RESET + "      |\n" +
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
            System.err.println("Enter age in digits");
        }
        return false;
    }
}


