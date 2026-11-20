
package com.rocs.rc.ohs.app.facade.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.app.facade.medicalrecord.MedicalRecordFacade;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import com.rocs.rc.ohs.data.dao.medicalrecord.impl.MedicalRecordDaoImpl;

import java.util.List;

/**
 * Handles patient medical record searches by calling the DAO.
 */
public class MedicalRecordFacadeImpl implements MedicalRecordFacade {

    /**
     * DAO used to access medical record data from the database.
     */
    private final MedicalRecordDao dao;

    /**
     * Creates the facade with the default DAO implementation.
     */
    public MedicalRecordFacadeImpl() {
        dao = new MedicalRecordDaoImpl();
    }

    /**
     * Creates the facade using the provided DAO.
     * This constructor is mainly used for testing.
     *
     * @param dao the DAO that handles medical record database operations
     */
    public MedicalRecordFacadeImpl(MedicalRecordDao dao) {
        this.dao = dao;
    }

    /**
     * Searches for patient medical records using a keyword.
     *
     * @param keyword the patient's name or identification number to search
     * @return a list of matching medical records
     */
    @Override
    public List<MedicalRecordDTO> search(String keyword) {
        return dao.search(keyword);
    }
}
