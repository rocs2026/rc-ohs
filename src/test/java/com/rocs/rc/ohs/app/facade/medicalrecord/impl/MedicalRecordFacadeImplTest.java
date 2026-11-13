package com.rocs.rc.ohs.app.facade.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.data.dao.medicalrecord.impl.MedicalRecordDaoImpl;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the medical record facade operations.
 */
class MedicalRecordFacadeImplTest {

    /**
     * Tests searching when matching medical records are found.
     */
    @Test
    void searchReturnsMatchingRecords() {
        List<MedicalRecordDTO> expectedRecords = new ArrayList<>();
        MedicalRecordDTO record = new MedicalRecordDTO();

        record.setPatientId("p1");
        record.setFirstName("Jowan");
        record.setLastName("Quirona");
        record.setMedicalRecordId("mr1");

        expectedRecords.add(record);

        try (MockedConstruction<MedicalRecordDaoImpl> mockedDAO =
                     mockConstruction(MedicalRecordDaoImpl.class,
                             (mock, context) -> when(mock.search("Jowan"))
                                     .thenReturn(expectedRecords))) {

            MedicalRecordFacadeImpl facade =
                    new MedicalRecordFacadeImpl();

            List<MedicalRecordDTO> actualRecords =
                    facade.search("Jowan");

            assertEquals(1, actualRecords.size());
            assertEquals(expectedRecords, actualRecords);
            assertEquals("p1", actualRecords.get(0).getPatientId());
            assertEquals("Jowan", actualRecords.get(0).getFirstName());

            verify(mockedDAO.constructed().get(0)).search("Jowan");
        }
    }

    /**
     * Tests searching when no matching medical records are found.
     */
    @Test
    void searchReturnsEmptyListWhenNoRecordsFound() {
        try (MockedConstruction<MedicalRecordDaoImpl> mockedDAO =
                     mockConstruction(MedicalRecordDaoImpl.class,
                             (mock, context) -> when(mock.search("Unknown"))
                                     .thenReturn(new ArrayList<>()))) {

            MedicalRecordFacadeImpl facade =
                    new MedicalRecordFacadeImpl();

            List<MedicalRecordDTO> actualRecords =
                    facade.search("Unknown");

            assertTrue(actualRecords.isEmpty());

            verify(mockedDAO.constructed().get(0))
                    .search("Unknown");
        }
    }

    /**
     * Tests that a null keyword is passed on to the DAO.
     */
    @Test
    void searchPassesNullKeywordToDao() {
        try (MockedConstruction<MedicalRecordDaoImpl> mockedDAO =
                     mockConstruction(MedicalRecordDaoImpl.class,
                             (mock, context) -> when(mock.search(null))
                                     .thenReturn(new ArrayList<>()))) {

            MedicalRecordFacadeImpl facade =
                    new MedicalRecordFacadeImpl();

            List<MedicalRecordDTO> actualRecords =
                    facade.search(null);

            assertTrue(actualRecords.isEmpty());

            verify(mockedDAO.constructed().get(0))
                    .search(null);
        }
    }

    /**
     * Tests that a DAO failure reaches the caller.
     */
    @Test
    void searchPropagatesDaoFailure() {
        try (MockedConstruction<MedicalRecordDaoImpl> mockedDAO =
                     mockConstruction(MedicalRecordDaoImpl.class,
                             (mock, context) -> when(mock.search("Jowan"))
                                     .thenThrow(new RuntimeException()))) {

            MedicalRecordFacadeImpl facade =
                    new MedicalRecordFacadeImpl();

            assertThrows(RuntimeException.class,
                    () -> facade.search("Jowan"));
        }
    }
}
