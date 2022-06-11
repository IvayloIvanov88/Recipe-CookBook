package src.services;


import static src.services.UserService.SCANNER;

public class PasswordFieldService {

    public static char[] readPassword (String prompt) {
        EraserThreadService et = new EraserThreadService(prompt);
        Thread mask = new Thread(et);
        mask.start();

        char[] password;

        password = SCANNER.nextLine().toCharArray();
        et.stopMasking();
        return password;
    }
    //String password = PasswordFieldService.readPassword("Enter a password:\n");
}