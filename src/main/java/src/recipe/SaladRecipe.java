package src.recipe;


public class SaladRecipe extends Recipe{

    public SaladRecipe(String name, int yield, int prepTime) {
        super(name, yield, prepTime);
    }

    public SaladRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Salad recipe\n" + super.toString();
    }

}
