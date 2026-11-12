package com.rocs.rc.ohs;

import com.rocs.rc.ohs.app.facade.login.LoginFacade;
import com.rocs.rc.ohs.app.facade.login.impl.LoginFacadeImpl;
import com.rocs.rc.ohs.app.model.login.Login;

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

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Clinic IS");
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
    //         } else if (!"0".equals(choice)) {
    //             System.out.println("Invalid choice.");
    //         }
    //     }
    // }
    //
    // private static void viewConsultations(Scanner sc,
    //                                       ConsultationFacade facade) {
    //     try {
    //         List<Consultation> consultations =
    //                 facade.getConsultationsByPatient(ask(sc, "Patient ID"));
    //         if (consultations.isEmpty()) {
    //             System.out.println("No consultations found.");
    //         }
    //         for (Consultation c : consultations) {
    //             System.out.println(c.getConsultationDate() + " | "
    //                     + c.getDiagnosis() + " | " + c.getTreatment());
    //             for (Prescription p
    //                     : facade.getPrescriptionsByConsultation(c.getId())) {
    //                 String medName = (p.getMedicineStock() != null
    //                         && p.getMedicineStock().getMedicine() != null)
    //                         ? p.getMedicineStock().getMedicine()
    //                                 .getMedicineName()
    //                         : "Unknown medicine";
    //                 System.out.println("    " + medName + " | "
    //                         + p.getDose() + " | " + p.getFrequency()
    //                         + " | qty " + p.getQuantityToDispense());
    //             }
    //         }
    //     } catch (IllegalArgumentException ex) {
    //         System.out.println(ex.getMessage());
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
    //     } catch (DateTimeParseException | NumberFormatException ex) {
    //         System.out.println("Invalid date or quantity.");
    //         return;
    //     }
    //     if (facade.saveConsultation(consultation, prescriptions) == null) {
    //         System.out.println("Could not save. Check the IDs and details.");
    //     } else {
    //         System.out.println("Consultation saved.");
    //     }
    // }
    //
    // private static String ask(Scanner sc, String label) {
    //     System.out.print(label + ": ");
    //     return sc.nextLine();
    // }
}
