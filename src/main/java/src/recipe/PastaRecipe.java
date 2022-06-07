package src.recipe;

public class PastaRecipe extends Recipe{

    public PastaRecipe(String name, int serving, int prepTime, double rating, int voteCount) {
        super(name, serving, prepTime, rating, voteCount);
    }

    public PastaRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Pasta recipe\n" + super.toString();
    }

}

