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

public class AuthService {

    private boolean isUserAuthenticated(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        User user = Demo.usersData.get(username);
        if (user != null){
            String salt = user.getSalt();
            String calculatedHash = getEncryptedPassword(password, salt);
            return calculatedHash.equals(user.getPassword());
        }else {
            return false;
        }
    }
    public boolean logIn(String username, String password) throws Exception {
        if (isUserAuthenticated(username, password)) {
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Sorry, wrong username or password.");
            return false;
        }
    }

    public boolean signUp(String userName, String password, String path) throws Exception {
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(password, salt);
        User user = new User();
        user.setPassword(encryptedPassword);
        user.setUsername(userName);
        user.setSalt(salt);
        if (usernameExists(userName, path)){
            System.out.println("Username already exists. Try with another one");
            return false;
        }else {
            saveUser(user, path);
            System.out.println("User registered successfully.");
            return true;
        }
    }
    private boolean usernameExists(String username, String path){
        List<String[]> fileData = CSVFileService.readFromCSV(path);
        return fileData.stream().anyMatch((r -> r[0].equalsIgnoreCase(username)));
    }


    public String getEncryptedPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        int iterations = 20000;

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

        byte[] encBytes = f.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(encBytes);

    }
    public String getNewSalt() throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    private void saveUser(User user, String path){
        String[] userData = new String[3];
        userData[0] = user.getUsername();
        userData[1] = user.getPassword();
        userData[2] = user.getSalt();
        CSVFileService.writeInCSV(path, userData);
        Demo.usersData.put(user.getUsername(), user);
    }
}
