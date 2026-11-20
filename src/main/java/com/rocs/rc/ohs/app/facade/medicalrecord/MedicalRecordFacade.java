package com.rocs.rc.ohs.app.facade.medicalrecord;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import java.util.List;

/**
 * Defines the medical record operations available to the application.
 */
public interface MedicalRecordFacade {

    /**
     * Searches for a patient by ID, name, student number or employee ID
     * and returns the patient's medical records.
     *
     * @param keyword the text to search for
     * @return the matching medical records, or an empty list if none match
     */
    List<MedicalRecordDTO> search(String keyword);
}
