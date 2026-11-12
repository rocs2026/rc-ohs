
package com.rocs.rc.ohs.data.dao.medicine.impl;

import com.rocs.rc.ohs.app.model.medicine.MedicineStock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MedicineStockDaoImplTest {

    private MedicineStockDaoImpl stockDao;

    @BeforeEach
    void setUp() {
        stockDao = new MedicineStockDaoImpl();
    }

    // TC1
    @Test
    void listStock() {
        List<MedicineStock> stocks = stockDao.getAllStock();

        assertNotNull(stocks);
        assertFalse(stocks.isEmpty());
    }

    // TC3
    @Test
    void stockDetails() {
        List<MedicineStock> stocks = stockDao.searchStock("Paracetamol");

        assertEquals(1, stocks.size());

        MedicineStock stock = stocks.get(0);

        assertEquals("B-001", stock.getBatchNumber());
        assertEquals(50, stock.getQuantityReceived());
        assertEquals("Enough", stock.getStatus());
    }

    // TC5
    @Test
    void searchKeyword() {
        List<MedicineStock> stocks = stockDao.searchStock("amox");

        assertEquals(1, stocks.size());
        assertEquals("Amoxicillin 500mg",
                stocks.get(0).getMedicine().getMedicineName());
    }

    // TC6
    @Test
    void searchNoMatch() {
        List<MedicineStock> stocks = stockDao.searchStock("zzzxxyy");

        assertNotNull(stocks);
        assertTrue(stocks.isEmpty());
    }
}
