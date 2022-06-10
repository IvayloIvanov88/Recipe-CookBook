package src.application;


import src.constants.Massages;
import src.entities.*;
import src.services.*;
import src.user.User;

import java.util.*;
import java.util.List;

import static src.constants.Constants.*;
import static src.services.CSVFileService.updateCSV;
import static src.services.RecipeService.*;
import static src.services.UserService.SCANNER;


public class Demo {

    public static Map<String, User> usersData = new HashMap<>();

    public static void main(String[] args) {
        ConsoleArtService artGen = new ConsoleArtService();

        List<String[]> usersFileData = CSVFileService.readFromCSV(USERS_DATA_PATH);
        UserService.addUserInList(usersData, usersFileData);

        User currentUser = null;

        MenuService.showLoginOptions();
        String choose = "";
        while (!(choose = SCANNER.nextLine()).trim().equalsIgnoreCase("exit")) {
            switch (choose) {
                case "1":
                    currentUser = AuthService.loginUser();
                    break;
                case "2":
                    if (AuthService.registerUser()) {
                        currentUser = AuthService.loginUser();
                    }
                    break;
                default:
                    System.out.println(ANSI_GREEN + "Try again, read carefully options." + ANSI_RESET);
            }
            if (currentUser != null) {
                break;
            }
            MenuService.showLoginOptions();

        }
        if (currentUser != null) {
            List<Recipe> unhiddenRecipes = new ArrayList<>();
            List<Recipe> hiddenRecipes = new ArrayList<>();

            List<String[]> unhiddenRecipesData = CSVFileService.readFromCSV(UNHIDDEN_RECIPE_PATH);
            List<String[]> hiddenRecipesData = CSVFileService.readFromCSV(HIDDEN_RECIPE_PATH);

            RecipeService.addRecipesInList(unhiddenRecipes, unhiddenRecipesData);
            RecipeService.addRecipesInList(hiddenRecipes, hiddenRecipesData);

            String defaultRecipesPath = UNHIDDEN_RECIPE_PATH;
            List<Recipe> defaultRecipes = unhiddenRecipes;
            List<String[]> defaultRecipesData = unhiddenRecipesData;

            MenuService.showOptions(Integer.toString(currentUser.getAge()));


            while (!(choose = SCANNER.nextLine()).trim().equalsIgnoreCase("exit")) {

                switch (choose) {
                    case "1":
                        printAllRecipesByName(defaultRecipes);
                        String userChooseRecipe = UserService.getUserChoose(Massages.ENTER_NUMBER_OF_RECIPE);
                        RecipeService.printRecipeByIndex(defaultRecipes, userChooseRecipe);
                        break;

                    case "2":
                        String recipeName = UserService.getUserChoose(Massages.ENTER_RECIPES_NAME);
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
                        printAllRecipesByName(defaultRecipes);
                        editRecipe(defaultRecipesPath, defaultRecipesData, defaultRecipes);
                        break;
                    case "4":
                        printAllRecipesByName(defaultRecipes);
                        choose = UserService.getUserChoose("Choose number to delete.");
                        try {
                            int userChoose = Integer.parseInt(choose);
                            CSVFileService.deleteFromCSV(defaultRecipesPath, userChoose);
                            defaultRecipes.remove(userChoose - 1);
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            System.err.println("Try with valid number!");
                        }
                        break;
                    case "5":
                        choose = UserService.getUserChoose(Massages.ENTER_RECIPES_NAME);

                        List<Recipe> recipeByPartOfName = getRecipeByPartOfName(defaultRecipes, choose);
                        if (recipeByPartOfName.isEmpty()) {
                            System.err.println(Massages.THERE_IS_NO_SUCH_RECIPE);
                            break;
                        }
                        if (recipeByPartOfName.size() == 1) {
                            recipeByPartOfName.forEach(System.out::println);
                            if (isRecipeSubjectOfEvaluation()) {

                                int oldVoteCount = recipeByPartOfName.get(0).getVoteCount();
                                double oldRecipeRate = recipeByPartOfName.get(0).getRating();
                                double newRecipeRate = evaluateRecipe(recipeByPartOfName);
                                String evaluateRecipeName = recipeByPartOfName.get(0).getName();
                                updateCSV(defaultRecipesPath, String.valueOf(recipeByPartOfName.get(0).getVoteCount()), String.valueOf(oldVoteCount), evaluateRecipeName);
                                updateCSV(defaultRecipesPath, String.valueOf(newRecipeRate), String.valueOf(oldRecipeRate), evaluateRecipeName);
                            }
                            break;
                        }
                        printAllRecipesByName(recipeByPartOfName);
                        String userChooseToView = UserService.getUserChoose(Massages.ENTER_NUMBER_OF_RECIPE);
                        RecipeService.printRecipeByIndex(recipeByPartOfName, userChooseToView);
                        break;
                    case "6":
                        choose = UserService.getUserChoose("Enter recipe`s type\nChoose one : Meet, meatless, dessert, salad, alaminut, pasta, soup.");

                        List<Recipe> recipeByFilter = getRecipeByFilter(defaultRecipes, choose);
                        if (recipeByFilter.isEmpty()) {
                            System.err.println(Massages.THERE_IS_NO_SUCH_RECIPE);
                            break;
                        }
                        if (recipeByFilter.size() == 1) {
                            recipeByFilter.forEach(System.out::println);
                            break;
                        }
                        printAllRecipesByName(recipeByFilter);
                        String userChooseToView1 = UserService.getUserChoose("Enter number of recipe that you want to view");
                        RecipeService.printRecipeByIndex(recipeByFilter, userChooseToView1);

                        break;

                    case "7":
                        if (UserService.validateUserAge(Integer.toString(currentUser.getAge()))) {
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
                MenuService.showOptions(Integer.toString(currentUser.getAge()));
            }
            artGen.draw("Bon Appetit", 13, ANSI_RED + "#" + ANSI_RESET);
        } else {
            System.out.println("\n\t\t\tAuthorization required");
            artGen.draw("Goodbye", 11, ANSI_GREEN + "$" + ANSI_RESET);

        }
    }
}