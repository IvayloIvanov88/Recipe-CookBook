package src.entities;

public class DessertRecipe extends Recipe{

    public DessertRecipe(String name, int serving, int prepTime, double rating, int voteCount, String owner) {
        super(name, serving, prepTime, rating, voteCount, owner);
    }

    public DessertRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Dessert recipe\n" + super.toString();
    }

}
