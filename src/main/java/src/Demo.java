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
//    public static String showParkingInformation() {
//        return """
//
//                        Welcome to Experian`s parking lot !
//                             To park a vehicle press: 1
//                     To take out a vehicle from parking slot press: 2
//                       For a list of all available vehicle press: 3
//                 For a list of characteristics on specific vehicle press: 4
//                                To leave press: 5
//                """;
//    }

}


