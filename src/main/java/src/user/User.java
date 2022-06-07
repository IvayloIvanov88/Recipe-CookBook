package src.user;

public class User {

    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private String password;

    public User(String username, String firstName, String lastName, int age, String password) {
        this.setUsername(username);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAge(age);
        this.setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0 || age > 120) {
            System.err.println("Invalid age.");
        }
        this.age = age;
    }


    public void setPassword(String password) {
        this.password = password;
    }

}
