package src.recipe;

import java.util.*;

import static java.lang.String.format;
import static src.utils.RecipeOperation.ANSI_RED;
import static src.utils.RecipeOperation.ANSI_RESET;

public abstract class Recipe {
    private String name;
    private List<String> ingredient;
    private int serving;
    private int prepTime;
    private Map<Integer, String> directions;

    protected Recipe(String name, int serving, int prepTime) {
        this.setName(name);
        this.setServing(serving);
        this.setPrepTime(prepTime);
        this.ingredient = new ArrayList<>();
        this.directions = new HashMap<>();
    }

    protected Recipe() {
        this.ingredient = new ArrayList<>();
        this.directions = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe recipe = (Recipe) o;
        return this.getName().equals(recipe.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {

        return format(
                ANSI_RED + "Recipe name is: " + ANSI_RESET + "%s\n" +
                        ANSI_RED + "ingredients are:\n" + ANSI_RESET + "%s.\n" +
                        ANSI_RED + "for: " + ANSI_RESET + "%d portions.\n" +
                        ANSI_RED + "Time for preparation: " + ANSI_RESET + "%d min.\n" +
                        ANSI_RED + "Preparation:" + ANSI_RESET + "\n%s",
                this.name,
                String.join(",", this.ingredient),
                this.serving,
                this.prepTime,
                getPreparationAsString());

    }

    protected StringBuilder getPreparationAsString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> entry : this.directions.entrySet()) {
            sb.append(String.format(ANSI_RED + "\tStep: %d " + ANSI_RESET + "-> %s\n", entry.getKey(), entry.getValue()));
        }
        return sb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("The Recipe should have a name.");
        } else {
            this.name = name;
        }
    }

    public List<String> getIngredient() {
        return Collections.unmodifiableList(this.ingredient);
    }


    public void addIngredient(String ingredient) {
        if (ingredient == null || ingredient.trim().isEmpty()) {
            System.err.println("The Recipe should have an ingredient.");
        } else {
            this.ingredient.add(ingredient);
        }
    }

    public void addAllIngredient(List<String> ingredient) {
        if (ingredient == null || ingredient.isEmpty()) {
            System.err.println("The Recipe should have an ingredient.");
        } else {
            this.ingredient.addAll(ingredient);
        }
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        if (serving <= 0) {
            System.err.println("The Recipe should have at least one serving. ");
        } else {
            this.serving = serving;
        }
    }

    public void setPrepTime(int prepTime) {
        if (prepTime <= 0) {
            System.err.println("The Recipe should have positive preparation time.");
        } else {
            this.prepTime = prepTime;
        }
    }

    public Map<Integer, String> getDirections() {
        return Collections.unmodifiableMap(directions);
    }

    public void setDirections(int step, String stepDescription) {
        this.directions.putIfAbsent(step, stepDescription);
    }

}
