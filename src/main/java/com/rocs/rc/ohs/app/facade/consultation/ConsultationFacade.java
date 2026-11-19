package com.rocs.rc.ohs.app.facade.consultation;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.app.model.personnel.Personnel;
import com.rocs.rc.ohs.app.model.prescription.Prescription;

import java.util.List;

/**
 * Defines the consultation operations of the clinic system.
 */
public interface ConsultationFacade {

    /**
     * Saves a consultation together with its prescriptions. Everything is
     * checked first, so nothing is saved when a required value is missing.
     *
     * @param consultation The consultation to save.
     * @param prescriptions The prescriptions of the consultation.
     * @return The saved {@link Consultation} with its generated ID, or
     *         null if a required value is missing or it could not be
     *         saved.
     */
    Consultation saveConsultation(Consultation consultation,
                                  List<Prescription> prescriptions);

    /**
     * Retrieves the saved consultations of a patient, latest first.
     *
     * @param patientId The UUID of the patient.
     * @return A list of consultations of the patient, or an empty list
     *         if they could not be read.
     * @throws IllegalArgumentException If the patient ID is missing.
     */
    List<Consultation> getConsultationsByPatient(String patientId)
            throws IllegalArgumentException;

    /**
     * Retrieves the prescriptions of a saved consultation.
     *
     * @param consultationId The UUID of the consultation.
     * @return A list of prescriptions of the consultation, or an empty
     *         list if they could not be read.
     * @throws IllegalArgumentException If the consultation ID is missing.
     */
    List<Prescription> getPrescriptionsByConsultation(
            String consultationId) throws IllegalArgumentException;

    /**
     * Finds the patient with the given full name, so the staff can type
     * the name of the patient instead of an ID.
     *
     * @param fullName The first, middle (optional) and last name.
     * @return The {@link Person} with that name.
     * @throws IllegalArgumentException If the name is missing, no patient
     *         has it, or more than one patient has it.
     */
    Person findPatientByName(String fullName)
            throws IllegalArgumentException;

    /**
     * Finds the clinic staff of the account that logged in, so the staff
     * who logged in is the one who handles the consultation.
     *
     * @param username The username of the account that logged in.
     * @return The {@link Personnel} of the account.
     * @throws IllegalArgumentException If the username is missing or the
     *         account is not a personnel.
     */
    Personnel findStaffByUsername(String username)
            throws IllegalArgumentException;

    /**
     * Finds the stock of the medicine with the given name, the one that
     * expires first, so the staff can prescribe the medicine by name.
     *
     * @param medicineName The name of the medicine.
     * @return The {@link MedicineStock} of the medicine.
     * @throws IllegalArgumentException If the name is missing or no
     *         medicine has it.
     */
    MedicineStock findMedicineStockByName(String medicineName)
            throws IllegalArgumentException;
}
