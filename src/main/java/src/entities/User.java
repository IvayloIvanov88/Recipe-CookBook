package src.entities;

import src.constants.Constants;
import src.constants.Massages;

public class User {

    private String username;
    private int age;
    private String password;
    private String salt;

    public User(String username, int age, String password, String salt) {
        this.setUsername(username);
        this.setAge(age);
        this.setPassword(password);
        this.setSalt(salt);
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < Constants.ZERO || age > Constants.MAXIMUM_USER_AGE) {
            System.err.println(Massages.INVALID_AGE);
        }
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
