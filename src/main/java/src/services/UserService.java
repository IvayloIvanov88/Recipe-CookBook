package src.services;

import org.jetbrains.annotations.NotNull;
import src.user.User;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static src.services.RecipeService.ANSI_RED;
import static src.services.RecipeService.ANSI_RESET;

public class UserService {
    private UserService() {
    }

    public static void addUserInList(Map<String, User> users, List<String[]> allData){
        User user = new User();
        if (allData.size() > 0){
            for (String[] row : allData) {
                String username = row[0];
                user.setUsername(username);
                user.setPassword(row[1]);
                user.setSalt(row[2]);

                users.put(username, user);
            }
        }else {
            System.out.println("User database is empty");
        }

    }

    static final String DELIMITER = "delimiter";
    public static final Scanner SCANNER = new Scanner(System.in);


    public static String getUserChoose(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
        return SCANNER.nextLine();
    }

    @NotNull
    public static String[] getUsersChooseFileToAdd(String recipeName, int voteCount, double rating) {
        StringBuilder sb = new StringBuilder();

        sb.append(recipeName).append(DELIMITER);

        System.out.println("How many portions ?");
        String toAppendYield = SCANNER.nextLine();
        sb.append(toAppendYield).append(DELIMITER);

        System.out.println("Preparation time ?");
        String toAppendPreparationTime = SCANNER.nextLine();
        sb.append(toAppendPreparationTime).append(DELIMITER);

        System.out.println("What are ingredients ?");
        String toAppendIngredients = SCANNER.nextLine();
        sb.append(toAppendIngredients).append(DELIMITER);

        System.out.println("What are the preparation steps ?");

        String toAppendThirdStep = SCANNER.nextLine();
        sb.append(toAppendThirdStep).append(DELIMITER);

        if (rating == 0 && voteCount == 0) {
            String toAppendDefaultRating = "0";
            sb.append(toAppendDefaultRating).append(DELIMITER);

            String toAppendDefaultVoteCount = "0";
            sb.append(toAppendDefaultVoteCount).append(DELIMITER);
        } else {
            String toAppendRating = String.valueOf(rating);
            sb.append(toAppendRating).append(DELIMITER);

            String toAppendVoteCount = String.valueOf(voteCount);
            sb.append(toAppendVoteCount).append(DELIMITER);
        }
        return sb.toString().split(DELIMITER);

    }

    public static boolean validateUserAge(String userChoose) {
        try {
            int age = Integer.parseInt(userChoose);
            return age >= 18;
        } catch (NumberFormatException e) {
            System.err.println("Enter age in digits.");
        }
        return false;
    }
}
