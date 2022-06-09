package src.services;

import java.io.IOException;

import static src.services.RecipeService.ANSI_RED;
import static src.services.RecipeService.ANSI_RESET;

public class MenuService {
    private MenuService() {
    }

    public static final String PRINT_LINE = "|----------------------------------------------|\n";

    public static void showOptions(String age) {
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
                appendHiddenRecipes(age) +
                PRINT_LINE + PRINT_LINE +
                "|\tTo exit type: exit                         |\n" +
                PRINT_LINE);


    }
    public static String appendHiddenRecipes(String userAge){
        boolean giveAccess = (Integer.parseInt(userAge) > 18);
        if (giveAccess){
            return  PRINT_LINE +
                    "|\t7. View hidden recipes                     |\n";
        }else{
            return "";
        }

    }
    public static void showLoginOptions(){
        System.out.println(PRINT_LINE +
                "|\t\t" + ANSI_RED + "Welcome to Experian`s recipe book !" + ANSI_RESET + "    |\n" +
                "|\t " + ANSI_RED + "Log in or create an account to continue" + ANSI_RESET + "   |\n" +
                PRINT_LINE +
                "|\t1. Log in                                  |\n" +
                PRINT_LINE +
                "|\t2. Sign up                                 |\n" +
                PRINT_LINE + PRINT_LINE +
                "|\tTo exit type: exit                         |\n" +
                PRINT_LINE);
    }
    public static void loginMessage(){
        System.out.println(PRINT_LINE +
                "|\t\t\t Log in to your account            |\n" +
                PRINT_LINE);
    }
    public static void signUpMessage(){
        System.out.println(PRINT_LINE +
                "|\t\t\t Sign up to access the app         |\n" +
                PRINT_LINE);
    }

    public static void pressEnterToContinue() {
        System.out.println("Press enter to continue");
        try{System.in.read();}
        catch(IOException ignored) {
        }

    }
}
