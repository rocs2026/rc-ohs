package com.rocs.rc.ohs.app.facade.medicalrecord.impl;

import com.rocs.rc.ohs.app.facade.medicalrecord.MedicalRecordFacade;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import com.rocs.rc.ohs.data.dao.medicalrecord.impl.MedicalRecordDaoImpl;

/**
 * Implementation of {@link MedicalRecordFacade}.
 */
public class MedicalRecordFacadeImpl implements MedicalRecordFacade {

    /** The DAO of Medical records. */
    private final MedicalRecordDao medicalRecordDao;

    /**
     * Initialize the facade with dao
     */
    public MedicalRecordFacadeImpl() {
        this(new MedicalRecordDaoImpl());
    }
    /**
     * Initializes the facade with a DAO.
     *
     * @param newMedicalRecordDao the medical record DAO.
     */
    public MedicalRecordFacadeImpl(MedicalRecordDao newMedicalRecordDao) {
        this.medicalRecordDao = newMedicalRecordDao;
    }
    /** {@inheritDoc}*/
    @Override
    public boolean updateMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord == null
            || medicalRecord.getId() == null
            || medicalRecord.getId().isBlank()) {
            throw new IllegalArgumentException(
                    "A medical record with an Id is required.");
        }
        return medicalRecordDao.updateMedicalRecord(medicalRecord);

    }
}
