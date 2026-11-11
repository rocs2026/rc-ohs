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
 * Tests the {@link MedicineDaoImpl} class.
 */
public class MedicineDaoImplTest {

    private static final String PARACETAMOL_ID =
            "ebb3209c-50ea-4fbd-8b7e-be4540ab5b57";
    private static final String AMOXICILLIN_ID =
            "36c00788-fcfd-485e-8ad3-de08fd49cb76";

    private MedicineDaoImpl medicineDao;

    @BeforeEach
    void setUp() {
        medicineDao = new MedicineDaoImpl();
    }

    @Test
    void listAll() {
        List<Medicine> medicines = medicineDao.getAllMedicine();

        assertNotNull(medicines);
        assertFalse(medicines.isEmpty());
    }

    @Test
    void listDetails() {
        List<Medicine> medicines = medicineDao.getAllMedicine();

        Medicine paracetamol = medicines.stream()
                .filter(m -> m.getId().equals(PARACETAMOL_ID))
                .findFirst()
                .orElse(null);

        assertNotNull(paracetamol);
        assertEquals("Paracetamol 500mg", paracetamol.getMedicineName());
        assertEquals("tablet", paracetamol.getUnit());
        assertEquals("Analgesic",
                paracetamol.getCategory().getCategoryName());
    }

    @Test
    void findById() {
        Medicine result = medicineDao.getMedicineById(PARACETAMOL_ID);

        assertNotNull(result);
        assertEquals("Paracetamol 500mg", result.getMedicineName());
        assertEquals("tablet", result.getUnit());
        assertEquals("Analgesic", result.getCategory().getCategoryName());
    }

    @Test
    void listMultiple() {
        List<Medicine> medicines = medicineDao.getAllMedicine();

        assertTrue(medicines.stream()
                .anyMatch(m -> m.getId().equals(PARACETAMOL_ID)));
        assertTrue(medicines.stream()
                .anyMatch(m -> m.getId().equals(AMOXICILLIN_ID)));
    }

    @Test
    void searchByKeyword() {
        List<Medicine> byName = medicineDao.searchMedicine("Paracetamol");
        List<Medicine> byKeyword = medicineDao.searchMedicine("amox");

        assertEquals(1, byName.size());
        assertEquals(PARACETAMOL_ID, byName.get(0).getId());
        assertEquals(1, byKeyword.size());
        assertEquals(AMOXICILLIN_ID, byKeyword.get(0).getId());
    }

    @Test
    void searchNoMatch() {
        List<Medicine> result = medicineDao.searchMedicine("zzzxxyy");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
