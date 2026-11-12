package com.rocs.rc.ohs.data.dao.medicine.impl;

import com.rocs.rc.ohs.app.model.medicine.Medicine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the MedicineDaoImpl class.
 */
public class MedicineDaoImplTest {

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
     * DAO used for medicine tests.
     */
    private MedicineDaoImpl medicineDao;

    @BeforeEach
    void setUp() {
        medicineDao = new MedicineDaoImpl();
    }

    /**
     * RCISD-5-TC1
     */
    @Test
    void listAll() {
        List<Medicine> medicines = medicineDao.getAllMedicine();

        assertNotNull(medicines);
        assertFalse(medicines.isEmpty());
    }

    /**
     * RCISD-5-TC2
     */
    @Test
    void listDetails() {
        List<Medicine> medicines = medicineDao.getAllMedicine();
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
     * RCISD-5-TC3
     */
    @Test
    void findById() {
        Medicine result = medicineDao.getMedicineById(PARACETAMOL_ID);

        assertNotNull(result);
        assertEquals("Paracetamol 500mg", result.getMedicineName());
        assertEquals("tablet", result.getUnit());
        assertEquals("Analgesic", result.getCategory().getCategoryName());
    }

    /**
     * RCISD-5-TC4
     */
    @Test
    void listMultiple() {
        List<Medicine> medicines = medicineDao.getAllMedicine();

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
     * RCISD-5-TC5 & RCISD-5-TC6
     */
    @Test
    void searchByKeyword() {
        List<Medicine> byName = medicineDao.searchMedicine("Paracetamol");
        List<Medicine> byKeyword = medicineDao.searchMedicine("amox");

        assertEquals(1, byName.size());
        assertEquals(PARACETAMOL_ID, byName.get(0).getId());

        assertEquals(1, byKeyword.size());
        assertEquals(AMOXICILLIN_ID, byKeyword.get(0).getId());
    }

    /**
     * RCISD-5-TC7
     */
    @Test
    void searchNoMatch() {
        List<Medicine> result = medicineDao.searchMedicine("zzzxxyy");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
