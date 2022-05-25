package src.recipe;

import java.util.*;

import static java.lang.String.format;

public  abstract class Recipe  {
    private String name;
    private List<String> ingredient;
    private int yield;
    private int prepTime;
    private Map<Integer, String> directions;

    public Recipe(String name, int yield, int prepTime) {
        this.setName(name);
        this.setYield(yield);
        this.setPrepTime(prepTime);
        this.ingredient = new ArrayList<>();
        this.directions = new HashMap<>();
    }

    public Recipe() {
        this.ingredient = new ArrayList<>();
        this.directions = new HashMap<>();
    }


    @Override
    public String toString() {

        return format("Recipe name is: %s\ningredients are: %s, for: %d portions.\nTime for preparation: %d min." +
                        "\nPreparation:\n%s",
                this.name,
                String.join(", ", this.ingredient),
                this.yield,
                this.prepTime,
                getPreparationAsStringBuilder());

    }

    protected StringBuilder getPreparationAsStringBuilder() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> entry : this.directions.entrySet()) {
            sb.append(String.format("Step: %d -> %s\n", entry.getKey(), entry.getValue()));
        }
        return sb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("The Recipe should have a name");
        } else {
            this.name = name;
        }
    }

    public List<String> getIngredient() {
        return Collections.unmodifiableList(this.ingredient);
    }


    public void addIngredient(String ingredient) {
        if (ingredient == null || ingredient.trim().isEmpty()) {
            throw new IllegalStateException("The Recipe should have an ingredient");
        } else {
            this.ingredient.add(ingredient);
        }
    }

    public void addAllIngredient(List<String> ingredient) {
        if (ingredient == null || ingredient.isEmpty()) {
            throw new IllegalStateException("The Recipe should have an ingredient");
        } else {
            this.ingredient.addAll(ingredient);
        }
    }
    public int getYield() {
        return yield;
    }

    public void setYield(int yield) {
        if (yield <= 0) {
            throw new IllegalStateException("The Recipe should have at least one serving ");
        } else {
            this.yield = yield;
        }
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        if (prepTime <= 0) {
            throw new IllegalStateException("The Recipe should have positive preparation time");
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

    public void setAllDirections(Map<Integer,String> directions){
        this.directions.putAll(directions);
    }


}
