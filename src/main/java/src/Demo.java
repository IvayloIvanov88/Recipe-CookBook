package src;


import org.jetbrains.annotations.NotNull;
import src.recipe.*;
import src.utils.Utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static src.utils.Utils.ANSI_RED;
import static src.utils.Utils.ANSI_RESET;

public class Demo {

    private static final String UNHIDDEN_RECIPE_PATH = "src/main/java/src/recipe.csv";
    private static final String HIDDEN_RECIPE_PATH = "src/main/java/src/hidden.csv";

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
                    unhiddenRecipes.forEach(r -> System.out.printf("%d. %s%n", count.addAndGet(1), r));
                    break;

                case "2":
                    addNewRecipe(scanner, unhiddenRecipes, UNHIDDEN_RECIPE_PATH);
                    break;

                case "3":
                    listAllRecipesByName(unhiddenRecipes);
                    choose = getUserChoose(scanner, "Choose recipe by name to change");
                    List<Recipe> recipeByName = getRecipeByName(unhiddenRecipes, choose);

                    String[] usersChooseFileToAdd = getUsersChooseFileToAdd(scanner);
                    unhiddenRecipes.removeAll(recipeByName);
                    Utils.addOneRecipeInList(unhiddenRecipes, usersChooseFileToAdd);
                    Utils.writeDataAtOnce(UNHIDDEN_RECIPE_PATH, usersChooseFileToAdd);

                    break;
                case "4":
                    listAllRecipesByName(unhiddenRecipes);
                    choose = getUserChoose(scanner, "Choose number to delete");
                    try {
                        int userChoose = Integer.parseInt(choose);
                        Utils.deleteRecord(UNHIDDEN_RECIPE_PATH, userChoose);
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
                        System.out.println("Welcome to the secret section with alcoholic beverages!\nTo view all hidden recipes press: 1\nTo add new recipe press: 2  ");
                        choose = scanner.nextLine().trim();
                        switch (choose){
                            case "1":
                                hiddenRecipes.forEach(System.out::println);
                                break;
                            case "2":
                                addNewRecipe(scanner, hiddenRecipes, HIDDEN_RECIPE_PATH);
                                break;
                        }

                    } else {
                        System.err.println("You are still very young");
                    }
                    break;
                default:
                    System.err.println("Try again, read carefully options. ");

            }
            showOptions();
        }
    }

    private static void listAllRecipesByName(List<Recipe> unhiddenRecipes) {
        AtomicInteger countRecipe = new AtomicInteger(0);
        unhiddenRecipes.forEach(r -> System.out.printf("%d. %s%n", countRecipe.addAndGet(1), r.getName()));
    }

    private static String getUserChoose(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }
    private static void addNewRecipe(Scanner scanner, List<Recipe> recipes, String path){
        String[] filesToAdd = getUsersChooseFileToAdd(scanner);
        if(Utils.addOneRecipeInList(recipes, filesToAdd)){
            Utils.writeDataAtOnce(path, filesToAdd);
            System.out.println("Recipe was added.");
        }else {
            System.err.println("Such recipe already exists.");
        }

    }

    @NotNull
    private static String[] getUsersChooseFileToAdd(Scanner scanner) {
        StringBuilder sb = new StringBuilder();

        System.out.println("Enter recipe`s name.");
        String toAppendName = scanner.nextLine();
        sb.append(toAppendName).append("delimiter");

        System.out.println("How many portions ?");
        String toAppendYield = scanner.nextLine();
        sb.append(toAppendYield).append("delimiter");

        System.out.println("Preparation time ?");
        String toAppendPreparationTime = scanner.nextLine();
        sb.append(toAppendPreparationTime).append("delimiter");

        System.out.println("What are ingredients ?");
        String toAppendIngredients = scanner.nextLine();
        sb.append(toAppendIngredients).append("delimiter");

        System.out.println("What are the preparation steps ?");

        String toAppendThirdStep = scanner.nextLine();
        sb.append(toAppendThirdStep).append("delimiter");

        return sb.toString().split("delimiter");
//        while (scanner.hasNext()){
//
//            if ((scanner.nextLine().trim().isEmpty())){
//                scanner.close();
//                return sb.toString().split("delimiter");
//            }
//            String toAppendFirstStep = scanner.nextLine();
//            sb.append(toAppendFirstStep); //.append("delimiter")
//
//        }
//
//        System.out.println("What is the second step of preparation ?");
//        String toAppendSecondStep = scanner.nextLine();
//        sb.append(toAppendSecondStep).append("delimiter");
//
//        System.out.println("What is the third step of preparation ?");
//        String toAppendThirdStep = scanner.nextLine();
//        sb.append(toAppendThirdStep).append("delimiter");
//

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
        System.out.println("\tWelcome to" + ANSI_RED + " Experian`s " + ANSI_RESET + "recipe book\nTo read all recipes press: 1\nTo add a recipe press: 2\n" +
                "To edit recipe press: 3\nTo delete recipe press: 4\nTo find specific recipe by name press: 5\n" +
                "To find specific recipe by type press: 6\nTo exit type: exit\n");

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


