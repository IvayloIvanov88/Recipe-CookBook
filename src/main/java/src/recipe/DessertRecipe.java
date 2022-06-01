package src.recipe;

public class DessertRecipe extends Recipe{

    public DessertRecipe(String name, int serving, int prepTime) {
        super(name, serving, prepTime);
    }

    public DessertRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Dessert recipe\n" + super.toString();
    }

}
