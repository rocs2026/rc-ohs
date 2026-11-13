package com.rocs.rc.ohs.app.medicine;

import com.rocs.rc.ohs.app.facade.medicine.impl.MedicineStockFacadeImpl;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;

import com.rocs.rc.ohs.data.dao.medicine.impl.MedicineStockDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the MedicineStockFacadeImpl class.
 */
public class MedicineStockFacadeImplTest {
    /** The facade being tested. */
    private MedicineStockFacadeImpl facade;

    @BeforeEach
    void setUp() {
        facade = new MedicineStockFacadeImpl(
                new MedicineStockDaoImpl());
    }
    /**
     * RCISD-5-TC1.
     */
    @Test
    void listAll() {
        List<MedicineStock> stocks = facade.getAllStock();

        assertNotNull(stocks);
        assertFalse(stocks.isEmpty());
    }

    /**
     * RCISD-5-TC2.
     */
    @Test
    void listDetails() {
        List<MedicineStock> stocks = facade.getAllStock();
        MedicineStock paracetamol = null;

        for (MedicineStock stock : stocks) {
            if (stock.getMedicine().getMedicineName()
                    .equals("Paracetamol 500mg")) {
                paracetamol = stock;
                break;
            }
        }

        assertNotNull(paracetamol);
        assertEquals("tablet", paracetamol.getMedicine().getUnit());
        assertEquals("Analgesic",
                paracetamol.getMedicine().getCategory().getCategoryName());
    }

    /**
     * RCISD-5-TC3.
     */
    @Test
    void stockDetails() {
        List<MedicineStock> stocks = facade.searchStock("Paracetamol");

        assertEquals(1, stocks.size());

        MedicineStock stock = stocks.get(0);

        assertEquals("B-001", stock.getBatchNumber());
        assertEquals(50, stock.getQuantityReceived());
        assertEquals("Enough", stock.getStatus());
    }

    /**
     * RCISD-5-TC4.
     */
    @Test
    void listMultiple() {
        List<MedicineStock> stocks = facade.getAllStock();

        boolean hasParacetamol = false;
        boolean hasAmoxicillin = false;

        for (MedicineStock stock : stocks) {
            String medicineName = stock.getMedicine().getMedicineName();

            if (medicineName.equals("Paracetamol 500mg")) {
                hasParacetamol = true;
            }

            if (medicineName.equals("Amoxicillin 500mg")) {
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
        List<MedicineStock> all = facade.getAllStock();
        List<MedicineStock> byKeyword = facade.searchStock("  amox  ");
        List<MedicineStock> byBlank = facade.searchStock("   ");

        assertEquals(1, byKeyword.size());
        assertEquals("Amoxicillin 500mg",
                byKeyword.get(0).getMedicine().getMedicineName());
        assertEquals(all.size(), byBlank.size());
    }

    /**
     * RCISD-5-TC7.
     */
    @Test
    void searchNoMatch() {
        List<MedicineStock> stocks = facade.searchStock("zzzxxyy");

        assertNotNull(stocks);
        assertTrue(stocks.isEmpty());
    }

    /**
     * Remaining Stock.
     */
    @Test
    void remainingStock() {
        int remaining = facade.getRemainingStock(
                "cff69d39-caa4-4a79-978b-8927f5b0c1a4");

        assertEquals(49, remaining);
    }
}

