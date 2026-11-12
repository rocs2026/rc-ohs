package com.rocs.rc.ohs.data.dao.medicine;

import com.rocs.rc.ohs.app.model.medicine.MedicineStock;

import java.util.List;

public interface MedicineStockDao {
    /**
     * Retrieves all medicine stock records.
     *
     * @return A {@link List} of all {@link MedicineStock} records.
     */
    List<MedicineStock> getAllStock();

    /**
     * Searches for medicine stock records that match the given keyword.
     *
     * @param keyword The keyword to search for in medicine
     * names or categories.
     * @return A {@link List} of matching {@link MedicineStock} records.
     */
    List<MedicineStock> searchStock(String keyword);
}
