package com.rocs.rc.ohs.data.dao.medicine.impl;

import com.rocs.rc.ohs.app.model.medicine.Medicine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

/**
 *  t
 *  Test the {@link MedicineDaoImpl}.
 */
public class MedicineDaoImplTest {
    private MedicineDaoImpl medicineDao;

    @BeforeEach
    void setUp() {
        medicineDao = new MedicineDaoImpl();
    }

    @Test
    void getAllMedicine_returnsList() {
        List<Medicine> medicines = medicineDao.getAllMedicine();

        assertNotNull(medicines);
    }

    @Test
    void getMedicineById_existingId_returnsMedicineDetails() {
        List<Medicine> medicines = medicineDao.getAllMedicine();
        assumeFalse(medicines.isEmpty(), "No medicine records in the database");

        Medicine expected = medicines.get(0);
        Medicine result = medicineDao.getMedicineById(expected.getId());

        assertNotNull(result);
        assertEquals(expected.getMedicineName(), result.getMedicineName());
        assertEquals(expected.getUnit(), result.getUnit());
        assertNotNull(result.getCategory());
    }

    @Test
    void getMedicineById_notExisting_returnsNull() {
        assertNull(medicineDao.getMedicineById("999999"));
    }

    @Test
    void searchMedicine_existingName_returnsMatchingRecords() {
        List<Medicine> medicines = medicineDao.getAllMedicine();
        assumeFalse(medicines.isEmpty(), "No medicine records in the database");

        String name = medicines.get(0).getMedicineName();
        List<Medicine> result = medicineDao.searchMedicine(name);

        assertFalse(result.isEmpty());
        assertTrue(result.stream()
                .anyMatch(m -> m.getMedicineName().equals(name)));
    }

    @Test
    void searchMedicine_noMatch_returnsEmptyList() {
        List<Medicine> result = medicineDao.searchMedicine("zzzxxyy-no-such-medicine");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}