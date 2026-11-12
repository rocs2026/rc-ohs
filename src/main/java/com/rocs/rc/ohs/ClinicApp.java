package com.rocs.rc.ohs;

import com.rocs.rc.ohs.app.facade.medicine.MedicineStockFacade;
import com.rocs.rc.ohs.app.facade.medicine.impl.MedicineStockFacadeImpl;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;

import java.util.List;
import java.util.Scanner;

/**
 * It is the console menu where the clinic staff checks the medicine
 * inventory.
 */
public final class ClinicApp {

    /** It reads whatever the staff types in the console. */
    private static final Scanner SCANNER = new Scanner(System.in);

    /** It gets the medicine stock records from the database. */
    private static final MedicineStockFacade FACADE =
            new MedicineStockFacadeImpl();

    /**
     * It stops anyone from creating an object of this class, since the
     * menu only uses static methods.
     */
    private ClinicApp() {
    }

    /**
     * It opens the inventory menu when the program runs.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        showMenu();
    }

    /**
     * It keeps showing the menu until the staff chooses 0 to exit.
     */
    private static void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("=== Medicine Inventory ===");
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
     * It lists every stock record, or says that the inventory is empty.
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
     * It asks for a keyword, then lists the stock records of the medicines
     * whose name or category matches it.
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
     * It prints one row for each stock record with its quantity, batch,
     * expiry date and status.
     *
     * @param stocks the records to list, from the inventory or the search.
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
     * It asks for a row number and shows the details of that record.
     * Pressing Enter goes back to the menu without choosing.
     *
     * @param stocks the records that were just listed in the table.
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
     * It prints each field of the chosen stock record on its own line.
     *
     * @param stock the record the staff picked from the table.
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
