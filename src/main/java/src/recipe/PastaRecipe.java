package src.recipe;

public class PastaRecipe extends Recipe{

    public PastaRecipe(String name, int serving, int prepTime) {
        super(name, serving, prepTime);
    }

    public PastaRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Pasta recipe\n" + super.toString();
    }

}

