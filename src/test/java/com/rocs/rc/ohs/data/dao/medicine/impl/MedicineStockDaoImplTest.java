package com.rocs.rc.ohs.data.dao.medicine.impl;

import com.rocs.rc.ohs.app.model.medicine.MedicineStock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the MedicineStockDaoImpl class.
 */
public class MedicineStockDaoImplTest {

    /** The DAO being tested. */
    private MedicineStockDaoImpl stockDao;

    @BeforeEach
    void setUp() {
        stockDao = new MedicineStockDaoImpl();
    }

    /**
     * RCISD-5-TC1.
     */
    @Test
    void listStock() {
        List<MedicineStock> stocks = stockDao.getAllStock();

        assertNotNull(stocks);
        assertFalse(stocks.isEmpty());
    }

    /**
     * RCISD-5-TC3.
     */
    @Test
    void stockDetails() {
        List<MedicineStock> stocks = stockDao.searchStock("Paracetamol");

        assertEquals(1, stocks.size());

        MedicineStock stock = stocks.get(0);

        assertEquals("B-001", stock.getBatchNumber());
        assertEquals(50, stock.getQuantityReceived());
        assertEquals("Enough", stock.getStatus());
    }

    /**
     * RCISD-5-TC5 & RCISD-5-TC6.
     */
    @Test
    void searchKeyword() {
        List<MedicineStock> stocks = stockDao.searchStock("amox");

        assertEquals(1, stocks.size());
        assertEquals("Amoxicillin 500mg",
                stocks.get(0).getMedicine().getMedicineName());
    }

    /**
     * RCISD-5-TC7.
     */
    @Test
    void searchNoMatch() {
        List<MedicineStock> stocks = stockDao.searchStock("zzzxxyy");

        assertNotNull(stocks);
        assertTrue(stocks.isEmpty());
    }
}
