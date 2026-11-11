package com.rocs.rc.ohs.data.dao.search;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import java.util.List;

/**
 * lets clinic staff search a patient and view their medical record
 */
public interface SearchDAO {
    /**
     * searches patient by name or id
     *
     * @param keyword the text to search for.
     * @return the matching patients, or an empty list if none match.
     */
    List<Person> search(String keyword);

    /**
     * Gets the medical records of a patient.
     *
     * @param patientId the ID of the patient.
     * @return the medical records, or an empty list if there are none.
     */
    List<MedicalRecord>viewMedicalRecord(String patientId);
}