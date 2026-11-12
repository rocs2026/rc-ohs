
package com.rocs.rc.ohs;

import com.rocs.rc.ohs.app.facade.login.LoginFacade;
import com.rocs.rc.ohs.app.facade.login.impl.LoginFacadeImpl;
import com.rocs.rc.ohs.app.facade.medicine.MedicineStockFacade;
import com.rocs.rc.ohs.app.facade.medicine.impl.MedicineStockFacadeImpl;
import com.rocs.rc.ohs.app.model.login.Login;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;

import java.util.List;
import java.util.Scanner;

/**
 * The main entry point of the clinic application.
 */
public final class ClinicApp {

    /** Reads user input from the console. */
    private static final Scanner SCANNER = new Scanner(System.in);

    /** Retrieves medicine stock records from the database. */
    private static final MedicineStockFacade FACADE =
            new MedicineStockFacadeImpl();

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

        System.out.println("Welcome to Clinic IS");
        System.out.print("USERNAME: ");
        String username = SCANNER.nextLine();
        System.out.print("PASSWORD: ");
        String password = SCANNER.nextLine();

        if (username.isEmpty()) {
            System.out.println("Please enter username");
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
        showMenu();
    }

    /**
     * Displays the inventory menu until the staff chooses to exit.
     */
    private static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("Medicine Inventory");
            System.out.println("1. View inventory");
            System.out.println("2. Search medicine");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = SCANNER.nextLine();

            if (choice.equals("1")) {
                viewInventory();
            } else if (choice.equals("2")) {
                searchMedicine();
            } else if (choice.equals("0")) {
                running = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Displays all medicine stock records.
     */
    private static void viewInventory() {
        List<MedicineStock> stocks = FACADE.getAllStock();

        if (stocks.isEmpty()) {
            System.out.println("No medicine records are available.");
        } else {
            showTable(stocks);
            chooseMedicine(stocks);
        }
    }

    /**
     * Searches medicine records using a keyword.
     */
    private static void searchMedicine() {
        System.out.print("Enter medicine name or keyword: ");
        String keyword = SCANNER.nextLine();

        List<MedicineStock> stocks = FACADE.searchStock(keyword);

        if (stocks.isEmpty()) {
            System.out.println("No matching medicine found.");
        } else {
            showTable(stocks);
        }
    }

    /**
     * Displays medicine stock records in a table.
     *
     * @param stocks the records to display.
     */
    private static void showTable(List<MedicineStock> stocks) {
        System.out.println();
        System.out.println("No.\tName\t\t\tCategory\tQty\tUnit\t"
                + "Batch\tExpiry\t\tStatus");

        for (int i = 0; i < stocks.size(); i++) {
            MedicineStock stock = stocks.get(i);
            System.out.println((i + 1) + "\t"
                    + stock.getMedicine().getMedicineName() + "\t"
                    + stock.getMedicine().getCategory().getCategoryName()
                    + "\t"
                    + stock.getQuantityReceived() + "\t"
                    + stock.getMedicine().getUnit() + "\t"
                    + stock.getBatchNumber() + "\t"
                    + stock.getExpirationDate().toLocalDate() + "\t"
                    + stock.getStatus());
        }
    }

    /**
     * Allows the user to select a medicine record.
     *
     * @param stocks the records displayed in the table.
     */
    private static void chooseMedicine(List<MedicineStock> stocks) {
        System.out.print("Enter the number to view details (Enter to skip): ");
        String input = SCANNER.nextLine();

        if (input.isEmpty()) {
            return;
        }

        try {
            int number = Integer.parseInt(input);

            if (number < 1 || number > stocks.size()) {
                System.out.println("Invalid number.");
            } else {
                showDetails(stocks.get(number - 1));
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid number.");
        }
    }

    /**
     * Displays the details of the selected medicine.
     *
     * @param stock the selected medicine stock record.
     */
    private static void showDetails(MedicineStock stock) {
        System.out.println();
        System.out.println("Medicine Details");
        System.out.println("Name     : "
                + stock.getMedicine().getMedicineName());
        System.out.println("Category : "
                + stock.getMedicine().getCategory().getCategoryName());
        System.out.println("Quantity : " + stock.getQuantityReceived());
        System.out.println("Unit     : " + stock.getMedicine().getUnit());
        System.out.println("Batch    : " + stock.getBatchNumber());
        System.out.println("Expiry   : "
                + stock.getExpirationDate().toLocalDate());
        System.out.println("Status   : " + stock.getStatus());
    }
}
