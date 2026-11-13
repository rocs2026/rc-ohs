package com.rocs.rc.ohs.app.facade.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.app.facade.medicalrecord.MedicalRecordFacade;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDAO;
import com.rocs.rc.ohs.data.dao.medicalrecord.impl.MedicalRecordDAOImpl;

import java.util.List;

/**
 * Implements the medical record operations by using the data access layer.
 */
public class MedicalRecordFacadeImpl implements MedicalRecordFacade {

    /**
     * The DAO used to read medical records.
     */
    private final MedicalRecordDAO medicalRecordDAO =
            new MedicalRecordDAOImpl();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MedicalRecordDTO> search(String keyword) {
        return medicalRecordDAO.search(keyword);
    }
}
