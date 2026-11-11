package com.rocs.rc.ohs.app.facade.medicine;
import com.rocs.rc.ohs.app.model.medicine.Medicine;
import java.util.List;

public interface MedicineFacade {
    /**
     * Retrieves all available medicine records.
     *
     * @return A {@link List} of all {@link Medicine} records.
     */
    List<Medicine> getAllMedicine();

    /**
     * Retrieves the medicine record that matches the given ID.
     *
     * @param id The ID of the medicine to look up.
     * @return The {@link Medicine} record matching the ID,
     * or null if not found.
     */
    Medicine getMedicineById(String id);

    /**
     * Searches for medicine records that match the given keyword.
     * Returns all medicine records if the keyword is blank.
     *
     * @param keyword The keyword to search for in medicine
     * names or categories.
     * @return A {@link List} of matching {@link Medicine} records.
     */
    List<Medicine> searchMedicine(String keyword);
}
