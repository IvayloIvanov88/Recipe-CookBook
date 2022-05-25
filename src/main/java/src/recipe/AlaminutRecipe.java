package src.recipe;


public class AlaminutRecipe extends Recipe{

    public AlaminutRecipe(String name, int yield, int prepTime) {
        super(name, yield, prepTime);
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
        if (prepTime > 10) {
            throw new IllegalStateException("The Alaminut recipe should be prepared within 10 minutes");
        } else {
            super.setPrepTime(prepTime);
        }
    }
}
