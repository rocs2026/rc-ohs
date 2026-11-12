package com.rocs.rc.ohs.app.facade.medicine;

import com.rocs.rc.ohs.app.model.medicine.MedicineStock;

import java.util.List;

public interface MedicineStockFacade {
    /**
     * Retrieves the whole medicine inventory.
     *
     * @return A {@link List} of all {@link MedicineStock} records.
     */
    List<MedicineStock> getAllStock();

    /**
     * Searches the medicine inventory. Returns the whole inventory
     * if the keyword is blank.
     *
     * @param keyword The keyword to search for in medicine
     * names or categories.
     * @return A {@link List} of matching {@link MedicineStock} records.
     */
    List<MedicineStock> searchStock(String keyword);
}
