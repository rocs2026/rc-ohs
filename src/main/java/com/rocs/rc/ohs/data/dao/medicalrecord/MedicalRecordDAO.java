package com.rocs.rc.ohs.data.dao.medicalrecord;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
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
}
