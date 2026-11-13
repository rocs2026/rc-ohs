package com.rocs.rc.ohs;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;

import com.rocs.rc.ohs.app.facade.login.LoginFacade;
import com.rocs.rc.ohs.app.facade.login.impl.LoginFacadeImpl;
import com.rocs.rc.ohs.app.facade.medicalrecord.MedicalRecordFacade;
import com.rocs.rc.ohs.app.facade.medicalrecord.impl.MedicalRecordFacadeImpl;
import com.rocs.rc.ohs.app.model.login.Login;

import java.util.List;
// import com.rocs.rc.ohs.app.facade.consultation.ConsultationFacade;
// import com.rocs.rc.ohs.app.facade.consultation.impl.ConsultationFacadeImpl;
// import com.rocs.rc.ohs.app.model.consultation.Consultation;
// import com.rocs.rc.ohs.app.model.enums.recordtype.RecordType;
// import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
// import com.rocs.rc.ohs.app.model.person.Person;
// import com.rocs.rc.ohs.app.model.personnel.Personnel;
// import com.rocs.rc.ohs.app.model.prescription.Prescription;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.time.format.DateTimeParseException;
// import java.util.ArrayList;
// import java.util.List;
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
     * Runs the clinic application and shows the main menu.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        LoginFacade loginFacade = new LoginFacadeImpl();

        System.out.println("Welcome to Clinic IS");
        System.out.println("[1] Login");
//        System.out.println("[2] Forgot password");
        System.out.print("Choose: ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1":
                login(sc, loginFacade);
                break;
//            case "2":
//                forgotPassword(sc, loginFacade);
//                break;
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
        // consultationMenu(sc);

    }

    // /**
    //  * Resets a forgotten password after
    //  * verifying the email and username.
    //  *
    //  * @param sc The scanner used to read input.
    //  * @param loginFacade The facade used for login operations.
    //  */
    // private static void forgotPassword(Scanner sc,
    //                                    LoginFacade loginFacade) {
    //     System.out.print("EMAIL: ");
    //     String email = sc.nextLine().trim();
    //     if (email.isEmpty()) {
    //         System.out.println("Please enter email");
    //         return;
    //     }
    //
    //     System.out.print("USERNAME: ");
    //     String username = sc.nextLine().trim();
    //     if (username.isEmpty()) {
    //         System.out.println("Please enter username");
    //         return;
    //     }
    //
    //     System.out.print("NEW PASSWORD: ");
    //     String newPassword = sc.nextLine();
    //     if (newPassword.isEmpty()) {
    //         System.out.println("Please enter new password");
    //         return;
    //     }
    //
    //     System.out.print("CONFIRM PASSWORD: ");
    //     String confirm = sc.nextLine();
    //     if (!newPassword.equals(confirm)) {
    //         System.out.println("Passwords do not match");
    //         return;
    //     }
    //
    //     if (loginFacade.forgotPassword(email, username, newPassword)) {
    //         System.out.println("Password updated successfully");
    //     } else {
    //       System.out.println("Email and username do not match any account");
    //     }
    // }

    // private static void consultationMenu(Scanner sc) {
    //     ConsultationFacade facade = new ConsultationFacadeImpl();
    //     String choice = "";
    //     while (!"0".equals(choice)) {
    //         System.out.println("1. Record a consultation");
    //         System.out.println("2. View patient medical records");
    //         System.out.println("0. Exit");
    //         choice = ask(sc, "Choose");
    //         if ("1".equals(choice)) {
    //             recordConsultation(sc, facade);
    //         } else if ("2".equals(choice)) {
    //             viewConsultations(sc, facade);
    //         }
    //     }
    // }
    //
    // private static void viewConsultations(Scanner sc,
    //                                       ConsultationFacade facade) {
    //     try {
    //         List<Consultation> consultations = facade
    //                 .getConsultationsByPatient(ask(sc, "Patient ID"));
    //         for (Consultation c : consultations) {
    //             System.out.println(c.getConsultationDate() + " | "
    //                     + c.getDiagnosis() + " | " + c.getTreatment());
    //             for (Prescription p
    //                     : facade.getPrescriptionsByConsultation(c.getId())) {
    //                 System.out.println("    " + p.getMedicineStock()
    //                         .getMedicine().getMedicineName() + " | "
    //                         + p.getDose() + " | " + p.getFrequency()
    //                         + " | qty " + p.getQuantityToDispense());
    //             }
    //         }
    //     } catch (IllegalArgumentException ex) {
    //         System.out.println("Cannot show records: " + ex.getMessage());
    //     }
    // }
    //
    // private static void recordConsultation(Scanner sc,
    //                                      ConsultationFacade facade) {
    //     Personnel staff = new Personnel();
    //     staff.setId(ask(sc, "Your staff ID"));
    //     Person patient = new Person();
    //     patient.setId(ask(sc, "Patient ID"));
    //     Consultation consultation = new Consultation();
    //     consultation.setStaff(staff);
    //     consultation.setPatient(patient);
    //     consultation.setRecordType(RecordType.CHECK_UP);
    //     List<Prescription> prescriptions = new ArrayList<>();
    //     try {
    //         consultation.setConsultationDate(LocalDateTime.parse(
    //                 ask(sc, "Date (yyyy-MM-dd HH:mm)"),
    //                 DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    //         consultation.setDiagnosis(ask(sc, "Diagnosis"));
    //         consultation.setTreatment(ask(sc, "Treatment plan"));
    //         while ("y".equalsIgnoreCase(
    //                 ask(sc, "Add a prescription? (y/n)"))) {
    //             MedicineStock stock = new MedicineStock();
    //             stock.setId(ask(sc, "Medicine stock ID"));
    //             Prescription prescription = new Prescription();
    //             prescription.setMedicineStock(stock);
    //             prescription.setDose(ask(sc, "Dose"));
    //             prescription.setFrequency(ask(sc, "Frequency"));
    //             prescription.setQuantityToDispense(
    //                     Integer.parseInt(ask(sc, "Quantity to dispense")));
    //             prescriptions.add(prescription);
    //         }
    //         if (facade.saveConsultation(consultation, prescriptions)
    //                 == null) {
    //             System.out.println("Could not save. Check the IDs.");
    //         } else {
    //             System.out.println("Consultation saved.");
    //         }
    //     } catch (DateTimeParseException | IllegalArgumentException ex) {
    //         System.out.println("Invalid input: " + ex.getMessage());
    //     }
    // }
    //
    // private static String ask(Scanner sc, String label) {
    //     System.out.print(label + ": ");
    //     return sc.nextLine();
    // }
}
