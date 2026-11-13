package com.rocs.rc.ohs.app.medicine;

import com.rocs.rc.ohs.app.facade.medicine.impl.MedicineFacadeImpl;
import com.rocs.rc.ohs.app.model.medicine.Medicine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the MedicineFacadeImpl class.
 */
public class MedicineFacadeImplTest {

    /**
     * ID of the Paracetamol medicine record.
     */
    private static final String PARACETAMOL_ID =
            "ebb3209c-50ea-4fbd-8b7e-be4540ab5b57";

    /**
     * ID of the Amoxicillin medicine record.
     */
    private static final String AMOXICILLIN_ID =
            "36c00788-fcfd-485e-8ad3-de08fd49cb76";

    /**
     * Facade used for medicine tests.
     */
    private MedicineFacadeImpl facade;

    @BeforeEach
    void setUp() {
        facade = new MedicineFacadeImpl();
    }

    /**
     * RCISD-5-TC1.
     */
    @Test
    void listAll() {
        List<Medicine> medicines = facade.getAllMedicine();

        assertNotNull(medicines);
        assertFalse(medicines.isEmpty());
    }

    /**
     * RCISD-5-TC2.
     */
    @Test
    void listDetails() {
        List<Medicine> medicines = facade.getAllMedicine();
        Medicine paracetamol = null;

        for (Medicine medicine : medicines) {
            if (medicine.getId().equals(PARACETAMOL_ID)) {
                paracetamol = medicine;
                break;
            }
        }

        assertNotNull(paracetamol);
        assertEquals("Paracetamol 500mg", paracetamol.getMedicineName());
        assertEquals("tablet", paracetamol.getUnit());
        assertEquals("Analgesic",
                paracetamol.getCategory().getCategoryName());
    }

    /**
     * RCISD-5-TC3.
     */
    @Test
    void findById() {
        Medicine result = facade.getMedicineById(
                " " + PARACETAMOL_ID + " ");

        assertNotNull(result);
        assertEquals("Paracetamol 500mg", result.getMedicineName());
        assertEquals("tablet", result.getUnit());
        assertEquals("Analgesic", result.getCategory().getCategoryName());
    }

    /**
     * RCISD-5-TC4.
     */
    @Test
    void listMultiple() {
        List<Medicine> medicines = facade.getAllMedicine();
        boolean hasParacetamol = false;
        boolean hasAmoxicillin = false;

        for (Medicine medicine : medicines) {
            if (medicine.getId().equals(PARACETAMOL_ID)) {
                hasParacetamol = true;
            }

            if (medicine.getId().equals(AMOXICILLIN_ID)) {
                hasAmoxicillin = true;
            }
        }

        assertTrue(hasParacetamol);
        assertTrue(hasAmoxicillin);
    }

    /**
     * RCISD-5-TC5 & RCISD-5-TC6.
     */
    @Test
    void searchKeyword() {
        List<Medicine> all = facade.getAllMedicine();
        List<Medicine> byKeyword =
                facade.searchMedicine("  Paracetamol  ");
        List<Medicine> byBlank = facade.searchMedicine("   ");

        assertEquals(1, byKeyword.size());
        assertEquals(PARACETAMOL_ID, byKeyword.get(0).getId());
        assertEquals(all.size(), byBlank.size());
    }

    /**
     * RCISD-5-TC7.
     */
    @Test
    void searchNoMatch() {
        List<Medicine> result = facade.searchMedicine("zzzxxyy");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
