package src.recipe;


public class SoupRecipe extends Recipe{

    public SoupRecipe(String name, int serving, int prepTime) {
        super(name, serving, prepTime);
    }

    public SoupRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Soup recipe\n" + super.toString();
    }

}
