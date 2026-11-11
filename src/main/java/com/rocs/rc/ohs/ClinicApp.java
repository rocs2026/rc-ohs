package com.rocs.rc.ohs;
import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.data.dao.medicine.impl.MedicineDaoImpl;
import java.util.List;
import java.util.Scanner;

/**
 * Displays medicine records using MedicineDaoImpl.
 */
public final class ClinicApp {

    /**
     * It is the scanner used to read user input.
     */
    private final Scanner scanner;

    /**
     * It is the DAO used to access medicine records.
     */
    private final MedicineDaoImpl medicineDao;

    /**
     * It is used to initialize the scanner and medicine DAO.
     */
    private ClinicApp() {
        scanner = new Scanner(System.in);
        medicineDao = new MedicineDaoImpl();
    }

    /**
     * It is the starting point of the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        ClinicApp app = new ClinicApp();
        app.showMenu();
    }

    /**
     * It is used to display the medicine inventory menu.
     */

    public void showMenu() {
        int choice;

        do {
            System.out.println("\nMEDICINE INVENTORY");
            System.out.println("1. View All Medicines");
            System.out.println("2. Search Medicine");
            System.out.println("3. View Medicine Details");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    displayAllMedicines();
                    break;
                case 2:
                    searchMedicine();
                    break;
                case 3:
                    displayMedicineDetails();
                    break;
                case 0:
                    System.out.println("Exiting Medicine Inventory");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 0);
    }

    private void displayAllMedicines() {
        List<Medicine> medicines = medicineDao.getAllMedicine();

        if (medicines.isEmpty()) {
            System.out.println("No medicine records found.");
            return;
        }

        for (Medicine medicine : medicines) {
            System.out.println("\nMedicine ID: " + medicine.getId());
            System.out.println("Medicine Name: " + medicine.getMedicineName());
            System.out.println("Category: "
                    + medicine.getCategory().getCategoryName());
            System.out.println("Unit: " + medicine.getUnit());
        }
    }

    private void searchMedicine() {
        System.out.print("Enter medicine name: ");
        String keyword = scanner.nextLine();

        List<Medicine> medicines = medicineDao.searchMedicine(keyword);

        if (medicines.isEmpty()) {
            System.out.println("No matching medicine found.");
            return;
        }

        for (Medicine medicine : medicines) {
            System.out.println("\nMedicine ID: " + medicine.getId());
            System.out.println("Medicine Name: " + medicine.getMedicineName());
            System.out.println("Category: "
                    + medicine.getCategory().getCategoryName());
            System.out.println("Unit: " + medicine.getUnit());
        }
    }

    private void displayMedicineDetails() {
        System.out.print("Enter Medicine ID: ");
        String id = scanner.nextLine();

        Medicine medicine = medicineDao.getMedicineById(id);

        if (medicine == null) {
            System.out.println("Medicine not found.");
            return;
        }

        System.out.println("\nMEDICINE DETAILS");
        System.out.println("Medicine ID: " + medicine.getId());
        System.out.println("Medicine Name: " + medicine.getMedicineName());
        System.out.println("Category: "
                + medicine.getCategory().getCategoryName());
        System.out.println("Unit: " + medicine.getUnit());
    }
}
