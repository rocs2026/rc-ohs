package com.rocs.rc.ohs.data.dao.medicalrecord;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import java.util.List;

/**
 * Defines the data access operations for medical records.
 */
public interface MedicalRecordDAO {

    /**
     * Searches for medical records matching the given keyword.
     *
     * @param keyword the search term
     * @return a list of matching medical record DTOs
     */
    List<MedicalRecordDTO> search(String keyword);

    /**
     * Updates the medical conditions and allergies of a medical record.
     *
     * @param medicalRecord the medical record holding the new values
     * @return true if a record was updated, false otherwise
     */
    boolean updateMedicalRecord(MedicalRecord medicalRecord);
}
