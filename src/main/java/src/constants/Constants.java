package src.constants;

import java.util.Scanner;

public class Constants {
    private Constants() {
    }
    public static final Scanner SCANNER = new Scanner(System.in);

    public static final String USERS_DATA_PATH = "src/main/java/src/users.csv";
    public static final String UNHIDDEN_RECIPE_PATH = "src/main/java/src/recipe.csv";
    public static final String HIDDEN_RECIPE_PATH = "src/main/java/src/hidden.csv";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static final String DELIMITER = "delimiter";
    public static final int ADULT_USER = 18;
    public static final int ZERO = 0;
    public static final int USER_MAX_RATE = 6;
    public static final int MAXIMUM_USER_AGE = 130;
    public static final int COCKTAIL_MAX_PREPARATION_TIME = 15;
    public static final int ALAMINUT_MAX_PREPARATION_TIME = 30;
    public static final int SALAD_MAX_PREPARATION_TIME = 60;
    public static final int PAGINATION_MIN_RECIPE = 30;



}
