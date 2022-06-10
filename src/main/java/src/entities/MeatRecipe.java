package src.entities;


public class MeatRecipe extends Recipe {

    public MeatRecipe(String name, int serving, int prepTime, double rating, int voteCount) {
        super(name, serving, prepTime, rating, voteCount);
    }

    public MeatRecipe() {
        super();
    }


    @Override
    public String toString() {
        return "Meat Recipe\n" + super.toString();
    }

}
