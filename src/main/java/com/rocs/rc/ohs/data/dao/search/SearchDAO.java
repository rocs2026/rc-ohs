
package com.rocs.rc.ohs.data.dao.search;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import java.util.List;

/**
 * Defines operations for searching patients and viewing medical records.
 */
public interface SearchDAO {

    /**
     * Searches for patients by name or student number.
     *
     * @param keyword the text to search for
     * @return matching patients, or an empty list if none match
     */
    List<Person> search(String keyword);

    /**
     * Retrieves the medical records of a patient.
     *
     * @param patientId the ID of the patient
     * @return the patient's medical records, or an empty list if none exist
     */
    List<MedicalRecord> viewMedicalRecord(String patientId);
}
