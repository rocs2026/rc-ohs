package com.rocs.rc.ohs;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.app.facade.login.LoginFacade;
import com.rocs.rc.ohs.app.facade.login.impl.LoginFacadeImpl;
import com.rocs.rc.ohs.app.facade.medicalrecord.MedicalRecordFacade;
import com.rocs.rc.ohs.app.facade.medicalrecord.impl.MedicalRecordFacadeImpl;
import com.rocs.rc.ohs.app.model.login.Login;

import java.util.List;
import java.util.Scanner;

/**
 * The main entry point of the clinic application.
 */
public final class ClinicApp {

    /**
     * The text the user types to leave the search screen.
     */
    private static final String EXIT_COMMAND = "exit";

    /**
     * The text shown when a value is missing.
     */
    private static final String NOT_AVAILABLE = "N/A";

    /**
     * Prevents instantiation of this class.
     */
    private ClinicApp() {
    }

    /**
     * Runs the clinic application and logs in the user.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Clinic IS");
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

        LoginFacade loginFacade = new LoginFacadeImpl();
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

        searchMedicalRecords(sc);
    }

    /**
     * Asks for a patient ID or name, searches, and shows the results until
     * the user types exit.
     *
     * @param sc the scanner used to read the user's input
     */
    private static void searchMedicalRecords(Scanner sc) {
        MedicalRecordFacade facade = new MedicalRecordFacadeImpl();
        boolean running = true;

        while (running) {
            System.out.print(
                    "Enter patient ID or name (type exit to quit): ");
            String input = sc.nextLine().trim();

            if (EXIT_COMMAND.equalsIgnoreCase(input)) {
                running = false;
            } else if (!input.isEmpty()) {
                showResults(facade, input);
            }
        }
    }

    /**
     * Searches for the keyword and prints the results, or a message when
     * nothing is found.
     *
     * @param facade the facade used to search
     * @param keyword the text to search for
     */
    private static void showResults(
            MedicalRecordFacade facade, String keyword) {
        try {
            List<MedicalRecordDTO> records = facade.search(keyword);

            if (records.isEmpty()) {
                System.out.println("No record found");
            } else {
                System.out.println(records.size() + " result(s) found");
                for (MedicalRecordDTO record : records) {
                    printRecord(record);
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Unable to search right now: "
                    + e.getMessage());
        }
    }

    /**
     * Prints one patient with the medical record and consultation details.
     *
     * @param record the record to print
     */
    private static void printRecord(MedicalRecordDTO record) {
        System.out.println("----------------------------------------");
        System.out.println("Patient ID         : "
                + show(record.getPatientId()));
        System.out.println("Name               : "
                + show(record.getFirstName()) + " "
                + show(record.getMiddleName()) + " "
                + show(record.getLastName()));
        System.out.println("Medical conditions : "
                + show(record.getMedicalConditions()));
        System.out.println("Allergies          : "
                + show(record.getAllergies()));
        System.out.println("Consultation date  : "
                + show(record.getConsultationDate()));
        System.out.println("Complaint          : "
                + show(record.getComplaint()));
        System.out.println("Diagnosis          : "
                + show(record.getDiagnosis()));
        System.out.println("Treatment          : "
                + show(record.getTreatment()));
    }

    /**
     * Converts a value to text, using a placeholder when it is missing.
     *
     * @param value the value to show
     * @return the value as text, or a placeholder if it is null
     */
    private static String show(Object value) {
        return value == null ? NOT_AVAILABLE : value.toString();
    }
}
