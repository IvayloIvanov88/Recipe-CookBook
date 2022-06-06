package src.utils;

import src.recipe.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RecipeOperation {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    static Scanner scanner = new Scanner(System.in);

    private RecipeOperation() {
    }

    public static List<Recipe> getRecipeByName(List<Recipe> recipes, String filter) {
        return recipes.stream()
                .filter(r -> Objects.equals(r.getName().toLowerCase(), filter.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<Recipe> getRecipeByFilter(List<Recipe> recipes, String filter) {

        return recipes.stream()
                .filter(r -> Objects.equals(r.getClass().getSimpleName().toLowerCase(), (filter + "recipe").toLowerCase()))
                .collect(Collectors.toList());
    }

    public static void listAllRecipesByName(List<Recipe> unhiddenRecipes) {
        AtomicInteger countRecipe = new AtomicInteger(0);
        unhiddenRecipes.forEach(r -> System.out.printf("%d. %s%n", countRecipe.addAndGet(1), r.getName()));
    }

    public static boolean isRecipeExist(List<String[]> fileData, String title) {
        return fileData.stream().anyMatch((r -> r[0].equalsIgnoreCase(title)));
    }

    public static boolean isRecipeContainsRecipeWithSameName(List<Recipe> recipes, String recipeName) {
        return recipes.stream().map(Recipe::getName).anyMatch(recipeName::equalsIgnoreCase);
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
                String[] split = nextLine[4].split("\\R");
                for (String s : split) {
                    recipe.setDirections(countSteps++, s);
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
            String[] split = data[4].split("\\.\\s+");
            for (String s : split) {
                recipe.setDirections(countSteps++, s);
            }

            recipes.add(recipe);
        }
    }

    private static Recipe getRecipeType(String name) {

        Recipe recipe = null;
        String[] fullName = name.split("\\s+");
        String firstWordOfName;
        RecipeSupplier recipeSupplier = new RecipeSupplier();

        for (int i = 0; i < fullName.length; i++) {
            firstWordOfName = fullName[i];

            recipe = recipeSupplier.supplyRecipe(firstWordOfName);
            if (recipe != null) {
                return recipe;
            }
        }
        System.out.println("Specified recipe type:\n Meat, Meatless, Soup, Salad, Dessert, Pasta or Alaminut");
        firstWordOfName = scanner.nextLine();
        return getRecipeType(firstWordOfName);
    }
}