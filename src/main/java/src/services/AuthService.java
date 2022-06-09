package src.services;

import src.application.Demo;
import src.user.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;

import static src.application.Demo.USERS_DATA_PATH;

public class AuthService {

    private boolean isUserAuthenticated(String username, String password) {
        User user = Demo.usersData.get(username);

        if (user != null) {
            String salt = user.getSalt();
            String calculatedHash = getEncryptedPassword(password, salt);
            return calculatedHash.equals(user.getPassword());
        } else {
            return false;
        }
    }

    public boolean logIn(String username, String password) {
        if (isUserAuthenticated(username, password)) {
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Sorry, wrong username or password.");
            return false;
        }
    }

    public boolean signUp(String userName, String password, int age, String path) {
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(password, salt);
        User user = new User();
        user.setPassword(encryptedPassword);
        user.setUsername(userName);
        user.setSalt(salt);
        user.setAge(age);
        if (usernameExists(userName, path)) {
            System.out.println("Username already exists. Try with another one");
            return false;
        } else {
            saveUser(user, path);
            System.out.println("User registered successfully.");
            return true;
        }
    }

    private boolean usernameExists(String username, String path) {
        List<String[]> fileData = CSVFileService.readFromCSV(path);
        return fileData.stream().anyMatch((r -> r[0].equalsIgnoreCase(username)));
    }


    public String getEncryptedPassword(String password, String salt) {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
        SecretKeyFactory f = null;
        try {
            f = SecretKeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.err.println();;
        }

        byte[] encBytes = new byte[0];
        try {
            assert f != null;
            encBytes = f.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            System.err.println();
        }
        return Base64.getEncoder().encodeToString(encBytes);

    }

    public String getNewSalt() {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            System.err.println();;
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
        String password = UserService.getUserChoose("Enter a password: ");
        int age = Integer.parseInt(UserService.getUserChoose("Enter your age: "));
        boolean isSignUp = false;
        try {
            isSignUp = authService.signUp(username, password, age, USERS_DATA_PATH);
        } catch (Exception e) {
            System.err.println();
        }
        return isSignUp;
    }

    public static User loginUser() {
        AuthService authService = new AuthService();
        MenuService.loginMessage();
        String username = UserService.getUserChoose("Enter a username: ");
        String password = UserService.getUserChoose("Enter a password: ");

        try {
            if (authService.logIn(username, password)) {
                return Demo.usersData.get(username);
            }
        } catch (Exception e) {
            System.err.println();
        }
        return null;
    }
}
