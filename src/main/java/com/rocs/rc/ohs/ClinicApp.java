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
