package com.rocs.rc.ohs.data.dao.medicalrecord;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import java.util.List;

/**
 * Defines the medical record operation for clinic staff.
 */
public interface MedicalRecordDAO {

    /**
     * Searches for a patient by ID, name, student number or employee ID
     * and returns the patient's medical records in a single query.
     *
     * @param keyword the text to search for
     * @return the matching results, each with the patient, medical record
     *         and consultation details, or an empty list if none match
     */
    List<MedicalRecordDTO> search(String keyword);
}
