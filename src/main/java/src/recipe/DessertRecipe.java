package src.recipe;

public class DessertRecipe extends Recipe{

    public DessertRecipe(String name, int serving, int prepTime, double rating, int voteCount) {
        super(name, serving, prepTime, rating, voteCount);
    }

    public DessertRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Dessert recipe\n" + super.toString();
    }

}
