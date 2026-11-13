package com.rocs.rc.ohs.app.facade.medicalrecord;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;

import java.util.List;

/**
 * Defines the operations for adding and viewing the medical records of
 * a patient.
 */
public interface AddMedRecordFacade {

    /**
     * Finds the patients that match a patient ID or a name.
     *
     * @param keyword the patient ID, or the full or partial name.
     * @return the matching patients, or an empty list if there is no match.
     */
    List<Person> findPatients(String keyword);

    /**
     * Adds a new medical record for a patient.
     *
     * @param patientId the ID of the patient.
     * @param medicalConditions the medical conditions of the patient.
     * @param allergies the allergies of the patient.
     * @return true if the medical record was added, otherwise false.
     */
    boolean addMedicalRecord(String patientId, String medicalConditions,
                             String allergies);

    /**
     * Retrieves the medical records of a patient.
     *
     * @param patientId the ID of the patient.
     * @return the medical records of the patient, or an empty list if
     *         the patient has none.
     */
    List<MedicalRecord> findMedicalRecords(String patientId);
}
