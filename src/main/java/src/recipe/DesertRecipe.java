package src.recipe;

public class DesertRecipe extends Recipe{

    public DesertRecipe(String name, int yield, int prepTime) {
        super(name, yield, prepTime);
    }

    public DesertRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Desert recipe\n" + super.toString();
    }

}
