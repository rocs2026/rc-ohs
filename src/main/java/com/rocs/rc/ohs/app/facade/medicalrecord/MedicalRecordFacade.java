package com.rocs.rc.ohs.app.facade.medicalrecord;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;

import java.util.List;

/**
 * Defines the operations for managing the medical records of a patient.
 */
public interface MedicalRecordFacade {

    /**
     * Finds patients by name.
     *
     * @param keyword the full or partial patient name.
     * @return the matching patients, or an empty list if there is no match.
     */
    List<Person> findPatients(String keyword);

    /**
     * Adds a new medical record for a patient.
     *
     * @param patientName the name of the patient.
     * @param medicalConditions the medical conditions of the patient.
     * @param allergies the allergies of the patient.
     * @return true if the medical record was added, otherwise false.
     */
    boolean addMedicalRecord(String patientName, String medicalConditions,
                             String allergies);

    /**
     * Retrieves the medical records of a patient by name.
     *
     * @param patientName the name of the patient.
     * @return the medical records of the patient, or an empty list if
     *         the patient has none.
     */
    List<MedicalRecord> findMedicalRecords(String patientName);
}
