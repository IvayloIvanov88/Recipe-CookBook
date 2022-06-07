package src.recipe;


public class SaladRecipe extends Recipe{

    public SaladRecipe(String name, int serving, int prepTime, double rating, int voteCount) {
        super(name, serving, prepTime, rating, voteCount);
    }

    public SaladRecipe() {
        super();
    }
    @Override
    public void setPrepTime(int prepTime) {
        if (prepTime > 20) {
            System.err.println("The salad recipe should be prepared within 20 minutes");
        } else {
            super.setPrepTime(prepTime);
        }
    }
    @Override
    public String toString() {
        return "Salad recipe\n" + super.toString();
    }

}
