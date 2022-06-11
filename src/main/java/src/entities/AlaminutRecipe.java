package src.entities;


public class AlaminutRecipe extends Recipe{

    public AlaminutRecipe(String name, int serving, int prepTime, double rating, int voteCount, String owner) {
        super(name, serving, prepTime, rating, voteCount, owner);
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
