package com.rocs.rc.ohs.data.dao.medicalrecord;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;

public interface MedicalRecordDao {
    /**
     * Update the modifiable fields (medical conditions and allergies) of an existing medical record.
     *
     * @param medicalRecord the medical record containing the updated values.
     * @return true if the medical record was updated false if no record with the given ID exists.
     */
    boolean updateMedicalRecord(MedicalRecord medicalRecord);
}
