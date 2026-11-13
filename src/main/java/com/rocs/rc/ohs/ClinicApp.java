
package com.rocs.rc.ohs;

import com.rocs.rc.ohs.app.facade.login.LoginFacade;
import com.rocs.rc.ohs.app.facade.login.impl.LoginFacadeImpl;
import com.rocs.rc.ohs.app.facade.medicine.MedicineStockFacade;
import com.rocs.rc.ohs.app.facade.medicine.impl.MedicineStockFacadeImpl;
import com.rocs.rc.ohs.app.model.login.Login;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.data.dao.medicine.impl.MedicineStockDaoImpl;

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
 * It is the main application class for the clinic system.
 */
public final class ClinicApp {

    /**
     * It is the private constructor to prevent instantiation.
     */
    private ClinicApp() {
    }

    /**
     * It is the main method to run the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Clinic IS");
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

        LoginFacade loginFacade = new LoginFacadeImpl();
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

        MedicineStockFacade medicineStockFacade =
                new MedicineStockFacadeImpl(
                        new MedicineStockDaoImpl());

        int choice;

        do {
            System.out.println("\nMedicine Inventory");
            System.out.println("1. View inventory");
            System.out.println("2. Search medicine");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Choose: ");
                scanner.next();
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                viewInventory(medicineStockFacade);
            } else if (choice == 2) {
                searchMedicineStock(scanner, medicineStockFacade);
            } else if (choice == 0) {
                System.out.println("Exiting application...");
            } else {
                System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        scanner.close();
    }

    /**
     * Displays all medicine inventory records with remaining quantity.
     *
     * @param facade the medicine stock facade
     */
    public static void viewInventory(MedicineStockFacade facade) {
        List<MedicineStock> stocks = facade.getAllStock();

        if (stocks.isEmpty()) {
            System.out.println("No stocks found.");
            return;
        }

        for (MedicineStock stock : stocks) {
            int remaining =
                    facade.getRemainingStock(stock.getId());

            displayStock(stock, remaining);
        }
    }

    /**
     * Searches medicine stock records and displays remaining quantity.
     *
     * @param scanner the scanner
     * @param facade the medicine stock facade
     */
    public static void searchMedicineStock(
            Scanner scanner, MedicineStockFacade facade) {

        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine();

        List<MedicineStock> stocks = facade.searchStock(keyword);

        if (stocks.isEmpty()) {
            System.out.println("No matching stocks found.");
            return;
        }

        for (MedicineStock stock : stocks) {
            int remaining =
                    facade.getRemainingStock(stock.getId());

            displayStock(stock, remaining);
        }
    }

    /**
     * Displays the details of a medicine stock record.
     *
     * @param stock the medicine stock record
     * @param remaining the remaining quantity
     */
    private static void displayStock(
            MedicineStock stock, int remaining) {

        System.out.println("Medicine: "
                + stock.getMedicine().getMedicineName()
                + " | Batch: " + stock.getBatchNumber()
                + " | Received: " + stock.getQuantityReceived()
                + " | Remaining: " + remaining
                + " | Status: " + stock.getStatus());
        // consultationMenu(sc);

    }

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
    //                                        ConsultationFacade facade) {
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
