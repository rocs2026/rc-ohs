package com.rocs.rc.ohs.app.facade.medicalrecord;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;

/**
 * Facade for clinic medical record.
 */
public interface MedicalRecordFacade {

    /**
     * Updates an existing medical record.
     *
     * @param medicalRecord record with updated values.
     * @return true if updated, false if id not found.
     * @throws IllegalArgumentException if the record or id is missing.
     */
    boolean updateMedicalRecord(MedicalRecord medicalRecord);

}
