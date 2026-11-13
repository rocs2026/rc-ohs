package com.rocs.rc.ohs.app.facade.medicine.impl;

import com.rocs.rc.ohs.app.facade.medicine.MedicineStockFacade;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.data.dao.medicine.MedicineStockDao;
import com.rocs.rc.ohs.data.dao.medicine.impl.MedicineStockDaoImpl;

import java.util.List;

/**
 * Implementation of MedicineStockFacade for managing medicine stock records.
 */
public class MedicineStockFacadeImpl implements MedicineStockFacade {

    /**
     * The data access object for medicine stock records.
     */
    private final MedicineStockDao medicineStockDao;

    /**
     * Constructs a new MedicineStockFacadeImpl and initializes the DAO.
     */
    public MedicineStockFacadeImpl() {
        this.medicineStockDao = new MedicineStockDaoImpl();
    }

    /**
     * Constructs a new MedicineStockFacadeImpl with a specified DAO.
     *
     * @param medicineStockDao the medicine stock DAO
     */
    public MedicineStockFacadeImpl(MedicineStockDao medicineStockDao) {
        this.medicineStockDao = medicineStockDao;
    }

    @Override
    public List<MedicineStock> getAllStock() {
        return medicineStockDao.getAllStock();
    }

    @Override
    public List<MedicineStock> searchStock(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStock();
        }
        return medicineStockDao.searchStock(keyword);
    }
}