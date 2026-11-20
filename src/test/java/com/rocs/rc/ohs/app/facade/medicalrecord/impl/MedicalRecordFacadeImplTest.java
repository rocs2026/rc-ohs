
package com.rocs.rc.ohs.app.facade.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the search operations of MedicalRecordFacadeImpl.
 */
class MedicalRecordFacadeImplTest {

    /**
     * Mock DAO used to simulate database operations.
     */
    private MedicalRecordDao dao;

    /**
     * Facade being tested.
     */
    private MedicalRecordFacadeImpl facade;

    /**
     * Creates the mock DAO and facade before each test.
     */
    @BeforeEach
    void setUp() {
        dao = mock(MedicalRecordDao.class);
        facade = new MedicalRecordFacadeImpl(dao);
    }

    /**
     * Tests if the facade returns the matching patient record.
     */
    @Test
    void testSearchFound() {
        MedicalRecordDTO record = new MedicalRecordDTO();
        record.setPatientId("p1");
        record.setFirstName("Jowan");
        record.setLastName("Quirona");
        record.setMedicalRecordId("mr1");

        when(dao.search("Jowan")).thenReturn(List.of(record));

        List<MedicalRecordDTO> results = facade.search("Jowan");

        assertEquals(1, results.size());
        assertEquals("Jowan", results.get(0).getFirstName());

        verify(dao).search("Jowan");
    }

    /**
     * Tests if the facade returns an empty list
     * when no matching patient is found.
     */
    @Test
    void testSearchEmpty() {
        when(dao.search("Unknown")).thenReturn(List.of());

        List<MedicalRecordDTO> results = facade.search("Unknown");

        assertTrue(results.isEmpty());
        verify(dao).search("Unknown");
    }

    /**
     * Tests if the facade passes a null keyword to the DAO.
     */
    @Test
    void testSearchNull() {
        when(dao.search(null)).thenReturn(List.of());

        List<MedicalRecordDTO> results = facade.search(null);

        assertTrue(results.isEmpty());
        verify(dao).search(null);
    }

    /**
     * Tests if an exception from the DAO is passed through the facade.
     */
    @Test
    void testSearchThrowsException() {
        when(dao.search("Jowan"))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class,
                () -> facade.search("Jowan"));
    }
}
