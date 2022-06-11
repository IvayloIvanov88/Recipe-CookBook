package src.entities;


public class SoupRecipe extends Recipe{

    public SoupRecipe(String name, int serving, int prepTime, double rating, int voteCount, String owner) {
        super(name, serving, prepTime, rating, voteCount, owner);
    }

    public SoupRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Soup recipe\n" + super.toString();
    }

}
