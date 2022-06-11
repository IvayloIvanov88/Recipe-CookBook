package src.entities;

public class MeatlessRecipe extends Recipe {

    public MeatlessRecipe(String name, int serving, int prepTime, double rating, int voteCount, String owner) {
        super(name, serving, prepTime, rating, voteCount, owner);
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
