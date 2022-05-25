package src;


import org.jetbrains.annotations.NotNull;
import src.recipe.*;
import src.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Demo {

    public static void main(String[] args) {
        String recipePath = "src/main/java/src/recipe.csv";
        String hiddenRecipePath = "src/main/java/src/hidden.csv";
        List<Recipe> unhiddenRecipes = new ArrayList<>();
        List<Recipe> hiddenRecipes = new ArrayList<>();

        List<String[]> unhiddenRecipeData = Utils.readingCSV(recipePath);
        List<String[]> hiddenRecipeData = Utils.readingCSV(hiddenRecipePath);

//        creatingRecipeList(recipes, allData, "SaladRecipe");
//        creatingRecipeList(recipes, allData, "PastaRecipe");
//        creatingRecipeList(recipes, allData, "AlaminutRecipe");
        Utils.creatingRecipeList(unhiddenRecipes, unhiddenRecipeData, "DesertRecipe");
//        creatingRecipeList(recipes, allData, "MeatRecipe");

        String[] filesToAdd = new String[]
                {"Gladen sym", "5", "20", "cheese,meet,vegetable,everything", "pick all products", "Cooking them", "serve and eat"};


//        writeDataAtOnce(recipePath, filesToAdd);
//        Utils.deleteRecord(recipePath,5);
        Utils.creatingRecipeList(hiddenRecipes,hiddenRecipeData,"MeatRecipe");

        System.out.println(unhiddenRecipes);
        System.out.println(hiddenRecipes);





    }


}


