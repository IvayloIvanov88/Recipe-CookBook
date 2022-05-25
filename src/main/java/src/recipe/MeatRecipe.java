package src.recipe;

import java.util.ArrayList;
import java.util.List;

public class MeatRecipe extends Recipe {

    public MeatRecipe(String name, int yield, int prepTime) {
        super(name, yield, prepTime);
    }

    public MeatRecipe() {
        super();
    }


    @Override
    public String toString() {
        return "Meat Recipe\n" + super.toString();
    }

}
