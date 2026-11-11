
package com.rocs.rc.ohs.data.dao.medicalRecord;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import java.util.List;

/**
 * Defines the medical record operation for clinic staff.
 */
public interface MedicalRecordDAO {

    /**
     * Searches for a patient by name or student number and returns the
     * patient's medical records in a single query.
     *
     * @param keyword the text to search for
     * @return the matching medical records, each with its patient and
     *         consultation, or an empty list if no patient matches
     */
    List<MedicalRecord> search(String keyword);
}