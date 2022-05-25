package src.recipe;

public class MeatlessRecipe extends Recipe {

    public MeatlessRecipe(String name, int yield, int prepTime) {
        super(name, yield, prepTime);
    }

    public MeatlessRecipe() {
        super();
    }

    @Override
    public String toString() {
        return "Recipe without meet\n" + super.toString();
    }

    @Override
    public void addIngredient(String ingredient){
        if(ingredient.equals("meat")){
            throw new IllegalArgumentException("This recipe is meatless");
        }else {
            super.addIngredient(ingredient);
        }

    }


}
