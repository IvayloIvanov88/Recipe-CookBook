package src.recipe;

public class DesertRecipe extends Recipe{

    public DesertRecipe(String name, int serving, int prepTime) {
        super(name, serving, prepTime);
    }

    public DesertRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Desert recipe\n" + super.toString();
    }

}
