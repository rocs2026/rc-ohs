
package com.rocs.rc.ohs.app.facade.medicine.impl;

import com.rocs.rc.ohs.app.facade.medicine.MedicineFacade;
import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.data.dao.medicine.MedicineDao;
import com.rocs.rc.ohs.data.dao.medicine.impl.MedicineDaoImpl;

import java.util.List;

/**
 * It is the implementation of the MedicineFacade interface.
 */
public class MedicineFacadeImpl implements MedicineFacade {

    /**
     * It is the data access object for medicine records.
     */
    private final MedicineDao medicineDao = new MedicineDaoImpl();

    /**
     * It is used to retrieve all medicine records.
     *
     * @return a list of all medicine records
     */
    @Override
    public List<Medicine> getAllMedicine() {
        return medicineDao.getAllMedicine();
    }

    /**
     * It is used to retrieve a medicine record by its ID.
     *
     * @param id the ID of the medicine
     * @return the medicine record if found, or null if the ID is invalid
     *         or empty
     */
    @Override
    public Medicine getMedicineById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        return medicineDao.getMedicineById(id.trim());
    }

    /**
     * It is used to search medicine records by keyword.
     *
     * @param keyword the medicine name or search keyword
     * @return a list of matching medicine records
     */
    @Override
    public List<Medicine> searchMedicine(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMedicine();
        }

        return medicineDao.searchMedicine(keyword.trim());
    }
}
