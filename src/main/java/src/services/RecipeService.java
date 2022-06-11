package src.services;

import src.constants.Constants;
import src.constants.Massages;
import src.entities.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static src.services.CSVFileService.updateCSV;


public class RecipeService {

    static Scanner scanner = new Scanner(System.in);

    private RecipeService() {
    }

    public static List<Recipe> getRecipeByName(List<Recipe> recipes, String filter) {
        return recipes.stream()
                .filter(r -> Objects.equals(r.getName().toLowerCase(), filter.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<Recipe> getRecipeByPartOfName(List<Recipe> recipes, String filter) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        String[] fullName = filter.split("\\s+");

        for (int i = 0; i < fullName.length; i++) {

            for (int j = 0; j < recipes.size(); j++) {
                String[] name = recipes.get(j).getName().split("\\s+");
                for (int k = 0; k < name.length; k++) {
                    if (fullName[i].equalsIgnoreCase(name[k])) {
                        filteredRecipes.add(recipes.get(j));
                    }
                }
            }
        }
        return filteredRecipes;
    }

    public static List<Recipe> getRecipeByFilter(List<Recipe> recipes, String filter) {

        return recipes.stream()
                .filter(r -> Objects.equals(r.getClass().getSimpleName().toLowerCase(), (filter + "recipe").toLowerCase()))
                .collect(Collectors.toList());
    }

    public static void printAllRecipesByName(List<Recipe> unhiddenRecipes) {
        AtomicInteger countRecipe = new AtomicInteger(0);
        unhiddenRecipes.forEach(r -> System.out.printf("%d. %s%n", countRecipe.addAndGet(1), r.getName()));
    }


    public static void printRecipeByIndex(List<Recipe> recipeByPartOfName, String userChooseToView) {
        try {
            System.out.println(recipeByPartOfName.get(Integer.parseInt(userChooseToView) - 1));
        } catch (NumberFormatException e) {
            System.err.println("Enter only digits.");
        } catch (IndexOutOfBoundsException e) {
            System.err.println(Massages.THERE_IS_NO_SUCH_RECIPE);
        }

    }

    public static boolean isRecipeExist(List<String[]> fileData, String title) {
        return fileData.stream().anyMatch((r -> r[0].equalsIgnoreCase(title)));
    }

    public static boolean isRecipeContainsRecipeWithSameName(List<Recipe> recipes, String recipeName) {
        return recipes.stream().map(Recipe::getName).anyMatch(recipeName::equalsIgnoreCase);
    }

    public static void editRecipe(String path, List<String[]> fileData, List<Recipe> recipes) {

        String recipeName = UserService.getUserChoose("Choose recipe by name to change.");

        if (RecipeService.isRecipeExist(fileData, recipeName) ||
                RecipeService.isRecipeContainsRecipeWithSameName(recipes, recipeName)) {

            List<Recipe> recipeByName = getRecipeByName(recipes, recipeName);

            double rating = recipeByName.get(0).getRating();
            int voteCount = recipeByName.get(0).getVoteCount();

            recipes.removeAll(recipeByName);
            String[] currentRecipe = fileData.stream().filter(r -> recipeName.equals(r[0])).findAny().orElse(null);
            int idx = fileData.indexOf(currentRecipe) + 1;
            CSVFileService.deleteFromCSV(path, idx);

            String[] usersChooseFileToAddInCSV = UserService.getUsersChooseFileToAdd(recipeName, voteCount, rating);
            CSVFileService.writeInCSV(path, usersChooseFileToAddInCSV);
            List<String[]> usersChooseFileToAddInList = new ArrayList<>(Collections.singleton(usersChooseFileToAddInCSV));
            addRecipesInList(recipes, usersChooseFileToAddInList);
            System.out.println(Constants.ANSI_GREEN + "Recipe edited successfully." + Constants.ANSI_RESET);
        } else {
            System.err.println(Massages.THERE_IS_NO_SUCH_RECIPE);
        }
    }

    public static void addRecipesInList(List<Recipe> recipes, List<String[]> allData) {
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
                String[] split = nextLine[4].split("\\.\\s+");
                for (String s : split) {
                    recipe.setDirections(countSteps++, s);
                }
                try {
                    recipe.setRating(Double.parseDouble(nextLine[5]));
                    recipe.setVoteCount(Integer.parseInt(nextLine[6]));
                } catch (NumberFormatException e) {
                    System.err.println("Enter digit in range 0 - 6");
                } catch (ArrayIndexOutOfBoundsException e) {
                    recipe.setRating(Double.parseDouble("0.00"));
                    recipe.setVoteCount(Integer.parseInt("0"));
                }

                recipes.add(recipe);
            }
        }
    }

    private static Recipe getRecipeType(String name) {

        Recipe recipe = null;
        String[] fullName = name.split("\\s+");
        String nameWordByWord;
        RecipeSupplier recipeSupplier = new RecipeSupplier();

        for (String s : fullName) {
            nameWordByWord = s;

            recipe = recipeSupplier.supplyRecipe(nameWordByWord);
            if (recipe != null) {
                return recipe;
            }
        }
        System.out.println("Specified recipe type:\n Meat, Meatless, Soup, Salad, Dessert, Pasta or Alaminut");
        nameWordByWord = scanner.nextLine();
        return getRecipeType(nameWordByWord);
    }

    public static boolean isRecipeSubjectOfEvaluation() {
        String userChoose = UserService.getUserChoose("Do you want to evaluate the recipe -> 'y' for yes or 'n' for no");
        return !userChoose.equalsIgnoreCase("n") && userChoose.equalsIgnoreCase("y");
    }

    public static double evaluateRecipe(Recipe recipe) {
        try {
            int userRating = Integer.parseInt(UserService.getUserChoose("Evaluate the recipe."));
            recipe.setUserRating(userRating);
        } catch (NumberFormatException e){
            System.err.println(Massages.ENTER_INTEGER_NUMBER);
        }

        return recipe.getRating();
    }

    public static void recipeEvaluateSystem(String defaultRecipesPath, Recipe recipesToEvaluate) {
        int oldVoteCount = recipesToEvaluate.getVoteCount();
        double oldRecipeRate = recipesToEvaluate.getRating();
        double newRecipeRate = evaluateRecipe(recipesToEvaluate);
        String evaluateRecipeName = recipesToEvaluate.getName();
        updateCSV(defaultRecipesPath, String.valueOf(recipesToEvaluate.getVoteCount()), String.valueOf(oldVoteCount), evaluateRecipeName);
        updateCSV(defaultRecipesPath, String.valueOf(newRecipeRate), String.valueOf(oldRecipeRate), evaluateRecipeName);
    }
}