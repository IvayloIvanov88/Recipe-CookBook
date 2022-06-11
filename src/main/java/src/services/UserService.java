package src.services;

import org.jetbrains.annotations.NotNull;
import src.constants.Constants;
import src.constants.Massages;
import src.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class UserService {
    private UserService() {
    }


    public static void addUserInList(Map<String, User> users, List<String[]> allData){
        if (!allData.isEmpty()){
            for (String[] row : allData) {
                User user = new User();
                String username = row[0];
                user.setUsername(username);
                user.setPassword(row[1]);
                user.setSalt(row[2]);
                user.setAge(Integer.parseInt(row[3]));
                users.put(username, user);
            }
        }else {
            System.out.println("User database is empty");
        }

    }

    public static final Scanner SCANNER = new Scanner(System.in);


    public static String getUserChoose(String message) {
        System.out.println(Constants.ANSI_RED + message +Constants.ANSI_RESET);
        return SCANNER.nextLine();
    }

    @NotNull
    public static String[] getUsersChooseFileToAdd(String recipeName, int voteCount, double rating, User currentUser) {
        StringBuilder sb = new StringBuilder();

        sb.append(recipeName).append(Constants.DELIMITER);

        System.out.println("How many portions ?");
        String toAppendYield = SCANNER.nextLine();
        sb.append(toAppendYield).append(Constants.DELIMITER);

        System.out.println("Preparation time ?");
        String toAppendPreparationTime = SCANNER.nextLine();
        sb.append(toAppendPreparationTime).append(Constants.DELIMITER);

        System.out.println("What are ingredients ?");
        String toAppendIngredients = SCANNER.nextLine();
        sb.append(toAppendIngredients).append(Constants.DELIMITER);

        System.out.println("What are the preparation steps ?");

        String toAppendThirdStep = SCANNER.nextLine();
        sb.append(toAppendThirdStep).append(Constants.DELIMITER);

        if (rating == 0 && voteCount == 0) {
            String toAppendDefaultRating = "0";
            sb.append(toAppendDefaultRating).append(Constants.DELIMITER);

            String toAppendDefaultVoteCount = "0";
            sb.append(toAppendDefaultVoteCount).append(Constants.DELIMITER);
        } else {
            String toAppendRating = String.valueOf(rating);
            sb.append(toAppendRating).append(Constants.DELIMITER);

            String toAppendVoteCount = String.valueOf(voteCount);
            sb.append(toAppendVoteCount).append(Constants.DELIMITER);
        }
        String toAppendUsername = currentUser.getUsername();
        sb.append(toAppendUsername).append(Constants.DELIMITER);

        return sb.toString().split(Constants.DELIMITER);

    }

    public static boolean validateUserAge(String userChoose) {
        try {
            int age = Integer.parseInt(userChoose);
            return age >= 18;
        } catch (NumberFormatException e) {
            System.err.println(Massages.ENTER_AGE_IN_DIGITS);
        }
        return false;
    }
}
