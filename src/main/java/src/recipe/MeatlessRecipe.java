package src.recipe;

public class MeatlessRecipe extends Recipe {

    public MeatlessRecipe(String name, int serving, int prepTime) {
        super(name, serving, prepTime);
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
            System.err.println("This recipe is meatless");
        }else {
            super.addIngredient(ingredient);
        }
    }


}
