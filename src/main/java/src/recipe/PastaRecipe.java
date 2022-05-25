package src.recipe;

public class PastaRecipe extends Recipe{

    public PastaRecipe(String name, int yield, int prepTime) {
        super(name, yield, prepTime);
    }

    public PastaRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Pasta recipe\n" + super.toString();
    }

}

