package src.recipe;


public class SoupRecipe extends Recipe{

    public SoupRecipe(String name, int yield, int prepTime) {
        super(name, yield, prepTime);
    }

    public SoupRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Soup recipe\n" + super.toString();
    }

}
