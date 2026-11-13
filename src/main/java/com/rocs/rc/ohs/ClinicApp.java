package com.rocs.rc.ohs;

import com.rocs.rc.ohs.app.facade.login.LoginFacade;
import com.rocs.rc.ohs.app.facade.login.impl.LoginFacadeImpl;
import com.rocs.rc.ohs.app.facade.medicalrecord.MedicalRecordFacade;
import com.rocs.rc.ohs.app.facade.medicalrecord.impl.MedicalRecordFacadeImpl;
import com.rocs.rc.ohs.app.model.login.Login;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import com.rocs.rc.ohs.data.dao.medicalrecord.impl.MedicalRecordDaoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * The main entry point of the clinic application.
 */
public final class ClinicApp {

    //    /**
//     * Prevents instantiation of this class.
//     */
//    private ClinicApp() {
//    }
//
//    /**
//     * Runs the clinic application and logs in the user.
//     *
//     * @param args The command line arguments.
//     */
//    public static void main(String[] args) {
//
//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("Welcome to Clinic IS");
//        System.out.print("USERNAME: ");
//        String username = sc.nextLine();
//        System.out.print("PASSWORD: ");
//        String password = sc.nextLine();
//
//        if (username.isEmpty()) {
//            System.out.println("Please enter username ");
//            return;
//        }
//
//        if (password.isEmpty()) {
//            System.out.println("Please enter password");
//            return;
//        }
//
//        LoginFacade loginFacade = new LoginFacadeImpl();
//        Login login = loginFacade.login(username, password);
//        if (login == null) {
//            System.out.println("Invalid username");
//            return;
//        }
//
//        if (!password.equals(login.getPassword())) {
//            System.out.println("Invalid password");
//            return;
//        }
//
//        System.out.println("Login successful");

    /**
     * Runs the update med record.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = ConnectionHelper.getConnection()) {
            MedicalRecordDao dao = new MedicalRecordDaoImpl(connection);
            MedicalRecordFacade facade = new MedicalRecordFacadeImpl(dao);
            updateMedicalRecord(scanner, facade);
        } catch (SQLException e) {
            System.out.println("Unable to connect to the database. ");
        }
    }

    /**
     * User input and updates a medical record if confirmed.
     *
     * @param scanner input scanner
     *
     * @param medicalRecordFacade facade for updating records
     */
    private static void updateMedicalRecord(
            Scanner scanner, MedicalRecordFacade medicalRecordFacade) {

        System.out.print("MEDICAL RECORD ID: ");
        String id = scanner.nextLine();
        if (id.isBlank()) {
            System.out.println("Please enter medical record id");
            return;
        }
        System.out.print("MEDICAL CONDITION: ");
        String medicalConditions = scanner.nextLine();
        System.out.print("ALLERGIES: ");
        String allergies = scanner.nextLine();

        System.out.print("Save changes? (Yes/No): ");
        String choice = scanner.nextLine();
        if (!choice.equalsIgnoreCase("Yes")) {
            System.out.println("Changes discarded. ");
            return;
        }

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(id);
        medicalRecord.setMedicalConditions(medicalConditions);
        medicalRecord.setAllergies(allergies);

        if (medicalRecordFacade.updateMedicalRecord(medicalRecord)) {
            System.out.println("Medical record updated");
        } else {
            System.out.println("No medical record found");
        }
    }
}
