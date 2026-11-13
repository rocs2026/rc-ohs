package com.rocs.rc.ohs.app.facade.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.app.facade.medicalrecord.MedicalRecordFacade;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import com.rocs.rc.ohs.data.dao.medicalrecord.impl.MedicalRecordDaoImpl;

import java.util.List;

/**
 * Implements the medical record operations by using the data access layer.
 */
public class MedicalRecordFacadeImpl implements MedicalRecordFacade {

    /**
     * The DAO used to read medical records.
     */
    private final MedicalRecordDao medicalRecordDAO =
            new MedicalRecordDaoImpl();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MedicalRecordDTO> search(String keyword) {
        return medicalRecordDAO.search(keyword);
    }
}
