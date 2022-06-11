package src.services;

import src.application.Demo;
import src.constants.Constants;
import src.entities.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static src.constants.Constants.USERS_DATA_PATH;
import static src.services.UserService.SCANNER;


public class AuthService {

    private boolean isUserAuthenticated(String username, char[] password) {
        User user = Demo.usersData.get(username);

        if (user != null) {
            String salt = user.getSalt();
            String calculatedHash = getEncryptedPassword(password, salt);
            Arrays.fill(password,'*');
            return calculatedHash.equals(user.getPassword());
        } else {
            return false;
        }
    }

    public boolean logIn(String username, char[] password) {
        if (isUserAuthenticated(username, password)) {
            Arrays.fill(password,'*');
            System.out.println(Constants.ANSI_GREEN + "Login successful." + Constants.ANSI_RESET);
            return true;
        } else {
            System.err.println("Sorry, wrong username or password.");
            return false;
        }
    }

    public boolean signUp(String userName, char[] password, int age, String path) {
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(password, salt);
        Arrays.fill(password,'*');

        User user = new User();
        user.setPassword(encryptedPassword);
        user.setUsername(userName);
        user.setSalt(salt);
        user.setAge(age);
        if (usernameExists(userName, path)) {
            System.err.println("Username already exists. Try with another one");
            return false;
        } else {
            saveUser(user, path);
            System.out.println(Constants.ANSI_GREEN + "User registered successfully." + Constants.ANSI_RESET);
            return true;
        }
    }

    private boolean usernameExists(String username, String path) {
        List<String[]> fileData = CSVFileService.readFromCSV(path);
        return fileData.stream().anyMatch((r -> r[0].equalsIgnoreCase(username)));
    }


    public String getEncryptedPassword(char[] password, String salt) {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password, saltBytes, iterations, derivedKeyLength);
        Arrays.fill(password,'*');
        SecretKeyFactory f = null;
        try {
            f = SecretKeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found");
        }

        byte[] encBytes = new byte[0];
        try {
            assert f != null;
            encBytes = f.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            System.err.println("Invalid key generated.");
        }
        return Base64.getEncoder().encodeToString(encBytes);

    }

    public String getNewSalt() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not found");
        }
        byte[] salt = new byte[8];
        assert random != null;
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private void saveUser(User user, String path) {
        String[] userData = new String[4];
        userData[0] = user.getUsername();
        userData[1] = user.getPassword();
        userData[2] = user.getSalt();
        userData[3] = Integer.toString(user.getAge());
        CSVFileService.writeInCSV(path, userData);
        Demo.usersData.put(user.getUsername(), user);
    }

    public static boolean registerUser() {
        AuthService authService = new AuthService();
        MenuService.signUpMessage();
        String username = UserService.getUserChoose("Enter a username: ");
        char[] password = validatePassword();
        System.out.println(password);

        int age = 0;
        boolean isAgeValid = false;
        while (!isAgeValid) {
            try {
                age = Integer.parseInt(UserService.getUserChoose("Enter your age: "));
                isAgeValid = age >= 0 && age < 120;
            } catch (NumberFormatException e) {
                System.err.println("Try again, enter age in digits.");
                isAgeValid = false;
            }
        }
        boolean isSignUp = false;
        try {
            isSignUp = authService.signUp(username, password, age, USERS_DATA_PATH);
        } catch (Exception e) {
            System.err.println("Sign up unsuccessful.");
        }
        return isSignUp;
    }

    private static char[] validatePassword() {
        final int MAX = 8;
        final int MIN_Uppercase = 2;
        final int MIN_Lowercase = 2;
        final int NUM_Digits = 2;
        final int SPECIAL = 2;
        int uppercaseCounter = 0;
        int lowercaseCounter = 0;
        int digitCounter = 0;
        int specialCounter = 0;

        System.err.println("Enter a password");
        char[] password = SCANNER.nextLine().toCharArray();

        for (char c : password) {
            if (Character.isUpperCase(c))
                uppercaseCounter++;
            else if (Character.isLowerCase(c))
                lowercaseCounter++;
            else if (Character.isDigit(c))
                digitCounter++;
            if (c >= 33 && c <= 46 || c == 64)
                specialCounter++;
        }

        if (password.length >= MAX && uppercaseCounter >= MIN_Uppercase
                && lowercaseCounter >= MIN_Lowercase && digitCounter >= NUM_Digits && specialCounter >= SPECIAL) {
            System.out.println("Valid Password");
            return password;
        } else {
            System.out.println("Your password does not contain the following:");
            if (password.length < MAX)
                System.out.println("At least 8 characters.");
            if (lowercaseCounter < MIN_Lowercase)
                System.out.println("Minimum 2 lowercase letters.");
            if (uppercaseCounter < MIN_Uppercase)
                System.out.println("Minimum 2 uppercase letters.");
            if (digitCounter < NUM_Digits)
                System.out.println("Minimum 2 digits.");
            if (specialCounter < SPECIAL)
                System.out.println("Minimum 2 special characters");

        }
        Arrays.fill(password,'*');
        return validatePassword();
    }

//    public static boolean isValidPassword(String password) {
//        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$");
//        Matcher matcher = pattern.matcher(password);
//        return matcher.matches();
//    }

    public static User loginUser() {
        AuthService authService = new AuthService();
        MenuService.loginMessage();
        String username = UserService.getUserChoose("Enter a username: ");
        char[] password = UserService.getUserChoose("Enter a password: ").toCharArray();
//        char[] password = PasswordFieldService.readPassword("Enter a password:\n");

        try {
            if (authService.logIn(username, password)) {
                return Demo.usersData.get(username);
            }

        } catch (Exception e) {
            System.err.println("Login unsuccessful.");
        }
        return null;
    }
}
