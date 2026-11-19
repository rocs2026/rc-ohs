package com.rocs.rc.ohs;


import com.rocs.rc.ohs.app.facade.login.LoginFacade;
import com.rocs.rc.ohs.app.facade.login.impl.LoginFacadeImpl;
import com.rocs.rc.ohs.app.model.login.Login;

// import com.rocs.rc.ohs.app.facade.consultation.ConsultationFacade;
// import com.rocs.rc.ohs.app.facade.consultation.impl.ConsultationFacadeImpl;
// import com.rocs.rc.ohs.app.model.consultation.Consultation;
// import com.rocs.rc.ohs.app.model.enums.recordtype.RecordType;
// import com.rocs.rc.ohs.app.model.person.Person;
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

//         consultationMenu(sc, username);

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
//
//     private static void consultationMenu(Scanner sc, String username) {
//         ConsultationFacade facade = new ConsultationFacadeImpl();
//         String choice = "";
//         while (!"0".equals(choice)) {
//             System.out.println("1. Record a consultation");
//             System.out.println("2. View patient medical records");
//             System.out.println("0. Exit");
//             choice = ask(sc, "Choose");
//             if ("1".equals(choice)) {
//                 recordConsultation(sc, facade, username);
//             } else if ("2".equals(choice)) {
//                 viewConsultations(sc, facade);
//             }
//         }
//     }
//
//     private static void viewConsultations(Scanner sc,
//                                           ConsultationFacade facade) {
//         try {
//             Person patient = facade
//                     .findPatientByName(ask(sc, "Patient name"));
//             for (Consultation c
//                     : facade.getConsultationsByPatient(patient.getId())) {
//                 System.out.println(c.getConsultationDate() + " | "
//                         + c.getDiagnosis() + " | " + c.getTreatment());
//                 for (Prescription p
//                         : facade.getPrescriptionsByConsultation(c.getId())) {
//                     System.out.println("    " + p.getMedicineStock()
//                             .getMedicine().getMedicineName() + " | "
//                             + p.getDose() + " | " + p.getFrequency()
//                             + " | qty " + p.getQuantityToDispense());
//                 }
//             }
//         } catch (IllegalArgumentException ex) {
//             System.out.println("Cannot show records: " + ex.getMessage());
//         }
//     }
//
//     private static void recordConsultation(Scanner sc,
//             ConsultationFacade facade, String username) {
//         List<Prescription> prescriptions = new ArrayList<>();
//         try {
//             Consultation consultation = new Consultation();
//             consultation.setStaff(facade.findStaffByUsername(username));
//             consultation.setPatient(
//                     facade.findPatientByName(ask(sc, "Patient name")));
//             consultation.setRecordType(RecordType.CHECK_UP);
//             consultation.setConsultationDate(askDate(sc));
//             consultation.setDiagnosis(ask(sc, "Diagnosis"));
//             consultation.setTreatment(ask(sc, "Treatment plan"));
//             while ("y".equalsIgnoreCase(
//                     ask(sc, "Add a prescription? (y/n)"))) {
//                 Prescription prescription = new Prescription();
//                 prescription.setMedicineStock(facade.findMedicineStockByName(
//                         ask(sc, "Medicine name")));
//                 prescription.setDose(ask(sc, "Dose"));
//                 prescription.setFrequency(ask(sc, "Frequency"));
//                 prescription.setQuantityToDispense(
//                         Integer.parseInt(ask(sc, "Quantity to dispense")));
//                 prescriptions.add(prescription);
//             }
//             if (facade.saveConsultation(consultation, prescriptions)
//                     == null) {
//                 System.out.println("Could not save the consultation.");
//             } else {
//                 System.out.println("Consultation saved.");
//             }
//         } catch (DateTimeParseException | IllegalArgumentException ex) {
//             System.out.println("Invalid input: " + ex.getMessage());
//         }
//     }
//
//     private static LocalDateTime askDate(Scanner sc) {
//         String text = ask(sc, "Date (yyyy-MM-dd HH:mm:ss, Enter = now)");
//         if (text.isBlank()) {
//             return LocalDateTime.now();
//         }
//         return LocalDateTime.parse(text.trim(), DateTimeFormatter
//                 .ofPattern("yyyy-MM-dd HH:mm[:ss]"));
//     }
//
//     private static String ask(Scanner sc, String label) {
//         System.out.print(label + ": ");
//         return sc.nextLine();
//     }
}
