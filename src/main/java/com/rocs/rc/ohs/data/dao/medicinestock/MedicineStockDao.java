package com.rocs.rc.ohs.data.dao.medicinestock;

import com.rocs.rc.ohs.app.model.medicine.MedicineStock;

import java.util.List;

/**
 * Defines the data access operations for the medicine stock inventory.
 */
public interface MedicineStockDao {

    /**
     * Retrieves all medicine stocks with their current quantities.
     *
     * @return The list of {@link MedicineStock}, or an empty list if none.
     */
    List<MedicineStock> findAll();

    /**
     * Retrieves the medicine stock with the given id.
     *
     * @param id The id of the medicine stock.
     * @return The {@link MedicineStock}, or null if it is not found.
     */
    MedicineStock findById(String id);

    /**
     * Retrieves the medicine stocks that reached their minimum stock level.
     *
     * @return The list of low stock {@link MedicineStock}.
     */
    List<MedicineStock> findLowStock();

    /**
     * Updates the received quantity of a medicine stock.
     *
     * @param id The id of the medicine stock.
     * @param quantity The new quantity, which must not be negative.
     * @return true if the quantity was saved.
     * @throws IllegalArgumentException if the quantity is negative or lower
     *         than the quantity already dispensed.
     */
    boolean updateQuantity(String id, int quantity);
}
