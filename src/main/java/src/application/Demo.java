package src.application;

import src.constants.Massages;
import src.entities.*;
import src.services.*;
import src.entities.User;

import java.util.*;
import java.util.List;

import static src.constants.Constants.*;
import static src.services.RecipeService.*;

public class Demo {
    public static final Map<String, User> usersData = new HashMap<>();

    public static void main(String[] args) {
        ConsoleArtService artGen = new ConsoleArtService();

        List<String[]> usersFileData = CSVFileService.readFromCSV(USERS_DATA_PATH);
        UserService.addUserInList(usersData, usersFileData);

        User currentUser = null;

        MenuService.showLoginOptions();
        String choose = "";
        while (!(choose = SCANNER.nextLine()).trim().equalsIgnoreCase(Massages.EXIT)) {
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

            RecipeService.addRecipesInList(unhiddenRecipes, unhiddenRecipesData, currentUser);
            RecipeService.addRecipesInList(hiddenRecipes, hiddenRecipesData, currentUser);

            String defaultRecipesPath = UNHIDDEN_RECIPE_PATH;
            List<Recipe> defaultRecipes = unhiddenRecipes;
            List<String[]> defaultRecipesData = unhiddenRecipesData;

            MenuService.showOptions(Integer.toString(currentUser.getAge()));

            while (!(choose = SCANNER.nextLine()).trim().equalsIgnoreCase(Massages.EXIT)) {

                switch (choose) {
                    case "1":
                        printAllRecipesByName(defaultRecipes);
                        String userChooseRecipe = UserService.getUserChoose(Massages.ENTER_NUMBER_OF_RECIPE);
                        RecipeService.printRecipeByIndex(defaultRecipes, userChooseRecipe);
                        if (isRecipeSubjectOfEvaluation())
                            recipeEvaluateSystem(defaultRecipesPath, defaultRecipes.get(Integer.parseInt(userChooseRecipe) - 1));
                        break;

                    case "2":
                        String recipeName = UserService.getUserChoose(Massages.ENTER_RECIPES_NAME);
                        if (!RecipeService.isRecipeExist(defaultRecipesData, recipeName)) {
                            if (RecipeService.isRecipeCocktailRecipeByName(recipeName) && currentUser.getAge() < ADULT_USER) {
                                System.err.println(Massages.YOU_ARE_YOUNG);
                                break;
                            } else {
                                String[] filesToAddInCSV = UserService.getUsersChooseFileToAdd(recipeName, 0, 0, currentUser);
                                CSVFileService.writeInCSV(defaultRecipesPath, filesToAddInCSV);
                                RecipeService.addRecipesInList(defaultRecipes, new ArrayList<>(Collections.singleton(filesToAddInCSV)), currentUser);
                                System.out.println(ANSI_GREEN + "Recipe was added." + ANSI_RESET);
                            }
                        } else {
                            System.err.println("There is already such recipe.");
                        }
                        break;
                    case "3":
                        printAllRecipesByName(defaultRecipes);
                        editRecipe(defaultRecipesPath, defaultRecipesData, defaultRecipes, currentUser);
                        break;
                    case "4":
                        printAllRecipesByName(defaultRecipes);
                        deleteRecipe(defaultRecipes, defaultRecipesPath, UserService.getUserChoose("Choose number to delete."), currentUser);
                        break;
                    case "5":
                        List<Recipe> recipeByPartOfName = getRecipeByPartOfName(defaultRecipes, UserService.getUserChoose(Massages.ENTER_RECIPES_NAME));
                        if (recipeByPartOfName.isEmpty()) {
                            System.err.println(Massages.THERE_IS_NO_SUCH_RECIPE);
                            break;
                        }
                        if (recipeByPartOfName.size() == 1) {
                            recipeByPartOfName.forEach(System.out::println);
                            if (isRecipeSubjectOfEvaluation())
                                recipeEvaluateSystem(defaultRecipesPath, recipeByPartOfName.get(0));
                            break;
                        }
                        printAllRecipesByName(recipeByPartOfName);
                        String userChooseToView = UserService.getUserChoose(Massages.ENTER_NUMBER_OF_RECIPE);
                        RecipeService.printRecipeByIndex(recipeByPartOfName, userChooseToView);
                        if (isRecipeSubjectOfEvaluation())
                            recipeEvaluateSystem(defaultRecipesPath, recipeByPartOfName.get(Integer.parseInt(userChooseToView) - 1));
                        break;
                    case "6":
                        List<Recipe> recipeByFilter = getRecipeByFilter(defaultRecipes,
                                UserService.getUserChoose("Enter recipe`s type\nChoose one : Meet, meatless, dessert, salad, alaminut, pasta, soup."));
                        printRecipesByFilter(recipeByFilter);
                        break;
                    case "7":
                        defaultRecipesPath = HIDDEN_RECIPE_PATH;
                        defaultRecipes = getRecipesByUser(hiddenRecipes, currentUser.getUsername());
                        defaultRecipesData = hiddenRecipesData;
                        System.out.println(ANSI_GREEN + "Redirecting to hidden recipes menu, all options are available\nTo go back type: back" + ANSI_RESET);
                        break;
                    case "8":
                        if (UserService.validateUserAge(Integer.toString(currentUser.getAge()))) {
                            System.out.println(ANSI_GREEN + "Welcome to the secret section with alcoholic beverages.\n" + ANSI_RESET);
                        } else {
                            System.err.println(Massages.YOU_ARE_YOUNG);
                            break;
                        }
                        List<Recipe> cocktails = getRecipeByFilter(defaultRecipes, "cocktail");
                        printRecipesByFilter(cocktails);
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