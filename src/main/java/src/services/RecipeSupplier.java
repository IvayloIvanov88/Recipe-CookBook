package src.services;

import src.recipe.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RecipeSupplier {

    private static final Map<String, Supplier<Recipe>> RECIPE_SUPPLIER;

    static {
        final Map<String, Supplier<Recipe>> recipesType = new HashMap<>();
        recipesType.put("meat", MeatRecipe::new);
        recipesType.put("duck", MeatRecipe::new);
        recipesType.put("pork", MeatRecipe::new);
        recipesType.put("chicken", MeatRecipe::new);

        recipesType.put("meatless", MeatlessRecipe::new);
        recipesType.put("vegetarian", MeatlessRecipe::new);
        recipesType.put("vegan", MeatlessRecipe::new);

        recipesType.put("salad", SaladRecipe::new);

        recipesType.put("soup", SoupRecipe::new);

        recipesType.put("jam", DessertRecipe::new);
        recipesType.put("dessert", DessertRecipe::new);

        recipesType.put("alaminut", AlaminutRecipe::new);
        recipesType.put("eggs", AlaminutRecipe::new);

        recipesType.put("dough", PastaRecipe::new);
        recipesType.put("pasta", PastaRecipe::new);
        recipesType.put("bread", PastaRecipe::new);
        recipesType.put("easter", PastaRecipe::new);
        recipesType.put("pancakes", PastaRecipe::new);

        recipesType.put("cocktail", CocktailRecipe::new);
        recipesType.put("cocktails", CocktailRecipe::new);
        recipesType.put("drink", CocktailRecipe::new);
        recipesType.put("drinks", CocktailRecipe::new);

        RECIPE_SUPPLIER = Collections.unmodifiableMap(recipesType);
    }

    public Recipe supplyRecipe(String recipeType) {
        Supplier<Recipe> recipes = RECIPE_SUPPLIER.get(recipeType.toLowerCase());

        if (recipes == null) {
            return null;
        }
        return recipes.get();
    }

}
