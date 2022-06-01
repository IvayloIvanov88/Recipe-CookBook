package src.recipe;


public class MeatRecipe extends Recipe {

    public MeatRecipe(String name, int serving, int prepTime) {
        super(name, serving, prepTime);
    }

    public MeatRecipe() {
        super();
    }


    @Override
    public String toString() {
        return "Meat Recipe\n" + super.toString();
    }

}
