package src.recipe;


public class AlaminutRecipe extends Recipe{

    public AlaminutRecipe(String name, int serving, int prepTime) {
        super(name, serving, prepTime);
    }

    public AlaminutRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Alaminut Recipe\n" + super.toString();
    }
    @Override
    public void setPrepTime(int prepTime) {
        if (prepTime > 15) {
            System.err.println("The Alaminut recipe should be prepared within 15 minutes");
        } else {
            super.setPrepTime(prepTime);
        }
    }
}
