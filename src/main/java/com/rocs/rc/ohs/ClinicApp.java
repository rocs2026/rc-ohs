
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
     *
     * @param args command-line arguments
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
