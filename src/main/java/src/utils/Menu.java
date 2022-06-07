package src.utils;

import static src.utils.RecipeOperation.ANSI_RED;
import static src.utils.RecipeOperation.ANSI_RESET;

public class Menu {
    public static final String PRINT_LINE = "|----------------------------------------------|\n";

    public static void showOptions() {
        System.out.println(PRINT_LINE +
                "|\t\t" + ANSI_RED + "Welcome to Experian`s recipe book !" + ANSI_RESET + "    |\n" +
                PRINT_LINE +
                "|\t1. Read all recipes                        |\n" +
                PRINT_LINE +
                "|\t2. Add a recipe                            |\n" +
                PRINT_LINE +
                "|\t3. Edit recipe                             |\n" +
                PRINT_LINE +
                "|\t4. Delete recipe                           |\n" +
                PRINT_LINE +
                "|\t5. Find specific recipe by name            |\n" +
                PRINT_LINE +
                "|\t6. Find specific recipe by type            |\n" +
                PRINT_LINE + PRINT_LINE +
                "|\tTo exit type: exit                         |\n" +
                PRINT_LINE);


    }
}
