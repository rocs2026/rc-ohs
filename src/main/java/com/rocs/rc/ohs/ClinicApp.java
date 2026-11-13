
package com.rocs.rc.ohs;


import com.rocs.rc.ohs.app.facade.login.LoginFacade;
import com.rocs.rc.ohs.app.facade.login.impl.LoginFacadeImpl;
import com.rocs.rc.ohs.app.model.login.Login;

import java.util.Scanner;

// Medicine Inventory imports - commented muna
// import com.rocs.rc.ohs.app.facade.medicine.MedicineStockFacade;
// import com.rocs.rc.ohs.app.facade.medicine.impl.MedicineStockFacadeImpl;
// import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
// import com.rocs.rc.ohs.data.dao.medicine.impl.MedicineStockDaoImpl;
// import java.util.List;

/**
 * It is the main application class for the clinic system.
 */
public final class ClinicApp {

    private ClinicApp() {
    }

    /**
     * Runs the main clinic application.
     * Runs the clinic application and shows the main menu.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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
        String username = scanner.nextLine();

        System.out.print("PASSWORD: ");
        String password = scanner.nextLine();

        if (username.isEmpty()) {
            System.out.println("Please enter username");
            scanner.close();
            return;
        }

        if (password.isEmpty()) {
            System.out.println("Please enter password");
            scanner.close();
            return;
        }

        Login login = loginFacade.login(username, password);

        if (login == null) {
            System.out.println("Invalid username");
            scanner.close();
            return;
        }

        if (!password.equals(login.getPassword())) {
            System.out.println("Invalid password");
            scanner.close();
            return;
        }

        System.out.println("Login successful");

        // Medicine Inventory section - commented muna

        // MedicineStockFacade medicineStockFacade =
        //         new MedicineStockFacadeImpl(
        //                 new MedicineStockDaoImpl());
        //
        // int choice;
        //
        // do {
        //     System.out.println("\nMedicine Inventory");
        //     System.out.println("1. View inventory");
        //     System.out.println("2. Search medicine");
        //     System.out.println("0. Exit");
        //     System.out.print("Choose: ");
        //
        //     while (!scanner.hasNextInt()) {
        //         System.out.print("Invalid input. Choose: ");
        //         scanner.next();
        //     }
        //
        //     choice = scanner.nextInt();
        //     scanner.nextLine();
        //
        //     if (choice == 1) {
        //         viewInventory(medicineStockFacade);
        //     } else if (choice == 2) {
        //         searchMedicineStock(scanner, medicineStockFacade);
        //     } else if (choice == 0) {
        //         System.out.println("Exiting application...");
        //     } else {
        //         System.out.println("Invalid choice. Try again.");
        //     }
        //
        // } while (choice != 0);

        scanner.close();
    }

    // Medicine Inventory methods - commented muna

    // public static void viewInventory(MedicineStockFacade facade) {
    //     List<MedicineStock> stocks = facade.getAllStock();
    //
    //     if (stocks.isEmpty()) {
    //         System.out.println("No stocks found.");
    //         return;
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
    //
    //     for (MedicineStock stock : stocks) {
    //         int remaining =
    //                 facade.getRemainingStock(stock.getId());
    //         displayStock(stock, remaining);
    //     }
    // }

    // public static void searchMedicineStock(
    //         Scanner scanner, MedicineStockFacade facade) {
    //
    //     System.out.print("Enter search keyword: ");
    //     String keyword = scanner.nextLine();
    //
    //     List<MedicineStock> stocks = facade.searchStock(keyword);
    //
    //     if (stocks.isEmpty()) {
    //         System.out.println("No matching stocks found.");
    //         return;
    //     }
    //
    //     for (MedicineStock stock : stocks) {
    //         int remaining =
    //                 facade.getRemainingStock(stock.getId());
    //         displayStock(stock, remaining);
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

    // /**
    //  * Displays the details of a medicine stock record.
    //  *
    //  * @param stock the medicine stock record
    //  * @param remaining the remaining quantity
    //  */
    // private static void displayStock(
    //         MedicineStock stock, int remaining) {
    //
    //     System.out.println("Medicine: "
    //             + stock.getMedicine().getMedicineName()
    //             + " | Batch: " + stock.getBatchNumber()
    //             + " | Received: " + stock.getQuantityReceived()
    //             + " | Remaining: " + remaining
    //             + " | Status: " + stock.getStatus());
    // }
}
