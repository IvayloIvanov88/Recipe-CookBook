package src.entities;

public class CocktailRecipe extends Recipe{

    public CocktailRecipe(String name, int serving, int prepTime, double rating, int voteCount, String owner) {
        super(name, serving, prepTime, rating, voteCount, owner);
    }

    public CocktailRecipe() {
        super();
    }

    @Override
    public void setPrepTime(int prepTime) {
        if (prepTime > 10) {
            System.err.println("The cocktail recipe should be prepared within 10 minutes");
        } else {
            super.setPrepTime(prepTime);
        }
    }

    @Override
    public String toString() {
        return "Cocktail recipe\n" + super.toString();
    }
}
