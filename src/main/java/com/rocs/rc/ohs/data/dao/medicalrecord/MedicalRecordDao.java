package com.rocs.rc.ohs.data.dao.medicalrecord;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import java.util.List;

/**
 * DAta access contract for handling patient medical records
 */
public interface MedicalRecordDAO {

    /**
     * Finds medical records matching a search keyword
     */
    List<MedicalRecordDTO> search(String keyword);

    /**
     * Udates specific medical details (conditions and allergies)
     */
    boolean updateMedicalRecord(MedicalRecord medicalRecord);
}
