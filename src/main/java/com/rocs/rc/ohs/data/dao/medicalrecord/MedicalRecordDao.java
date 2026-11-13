package com.rocs.rc.ohs.data.dao.medicalrecord;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;

import java.util.List;

/**
 * Defines the data access operations for medical records.
 */
public interface MedicalRecordDao {

    /**
     * Retrieves all medical records of a patient using the patient's name.
     *
     * @param patientName the name of the patient.
     * @return the medical records of the patient, or an empty list if
     *         the patient has none.
     */
    List<MedicalRecord> findByPatientName(String patientName);

    /**
     * Adds a medical record.
     *
     * @param medicalRecord the medical record to add.
     * @return true if successful, otherwise false.
     */
    boolean addMedicalRecord(MedicalRecord medicalRecord);

    /**
     * Updates modifiable fields of an existing medical record.
     *
     * @param medicalRecord the record containing the updated values.
     * @return true if the record was updated false if no record
     *         with the given ID exists.
     */
    boolean updateMedicalRecord(MedicalRecord medicalRecord);
}
