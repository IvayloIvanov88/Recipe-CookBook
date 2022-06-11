package src.entities;

public class DoughRecipe extends Recipe{

    public DoughRecipe(String name, int serving, int prepTime, double rating, int voteCount, String owner) {
        super(name, serving, prepTime, rating, voteCount, owner);
    }

    public DoughRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Dough recipe\n" + super.toString();
    }

}

