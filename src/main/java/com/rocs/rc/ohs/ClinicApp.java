package com.rocs.rc.ohs;

import com.rocs.rc.ohs.app.facade.login.LoginFacade;
import com.rocs.rc.ohs.app.facade.login.impl.LoginFacadeImpl;
import com.rocs.rc.ohs.app.model.login.Login;

import java.util.Scanner;

/**
 * The main entry point of the clinic application.
 */
public final class ClinicApp {

    /**
     * Prevents instantiation of this class.
     */
    private ClinicApp() {
    }

    /**
     * Runs the clinic application and shows the main menu.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        LoginFacade loginFacade = new LoginFacadeImpl();

        System.out.println("Welcome to Clinic IS");
        System.out.println("[1] Login");
        System.out.println("[2] Forgot password");
        System.out.print("Choose: ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1":
                login(sc, loginFacade);
                break;
            case "2":
                forgotPassword(sc, loginFacade);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    /**
     * Logs in the user.
     *
     * @param sc The scanner used to read input.
     * @param loginFacade The facade used for login operations.
     */
    private static void login(Scanner sc, LoginFacade loginFacade) {
        System.out.print("USERNAME: ");
        String username = sc.nextLine();
        System.out.print("PASSWORD: ");
        String password = sc.nextLine();

        if (username.isEmpty()) {
            System.out.println("Please enter username ");
            return;
        }

        if (password.isEmpty()) {
            System.out.println("Please enter password");
            return;
        }

        Login login = loginFacade.login(username, password);
        if (login == null) {
            System.out.println("Invalid username");
            return;
        }

        if (!password.equals(login.getPassword())) {
            System.out.println("Invalid password");
            return;
        }

        System.out.println("Login successful");
    }

    /**
     * Resets a forgotten password after
     * verifying the email and username.
     *
     * @param sc The scanner used to read input.
     * @param loginFacade The facade used for login operations.
     */
    private static void forgotPassword(Scanner sc,
                                       LoginFacade loginFacade) {
        System.out.print("EMAIL: ");
        String email = sc.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("Please enter email");
            return;
        }

        System.out.print("USERNAME: ");
        String username = sc.nextLine().trim();
        if (username.isEmpty()) {
            System.out.println("Please enter username");
            return;
        }

        System.out.print("NEW PASSWORD: ");
        String newPassword = sc.nextLine();
        if (newPassword.isEmpty()) {
            System.out.println("Please enter new password");
            return;
        }

        System.out.print("CONFIRM PASSWORD: ");
        String confirm = sc.nextLine();
        if (!newPassword.equals(confirm)) {
            System.out.println("Passwords do not match");
            return;
        }

        if (loginFacade.forgotPassword(email, username, newPassword)) {
            System.out.println("Password updated successfully");
        } else {
            System.out.println("Email and username do not match any account");
        }
    }
}