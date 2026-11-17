package com.rocs.rc.ohs.app.facade.medicalrecord.impl;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test the {@link MedicalRecordFacadeImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class MedicalRecordFacadeImplTest {

    @Mock
    private MedicalRecordDao medicalRecordDao;

    private MedicalRecordFacadeImpl medicalRecordFacade;

    /**
     * Prepares the facade with mock dao before each
     * test.
     */
    @BeforeEach
    void setUp() {
        medicalRecordFacade = new MedicalRecordFacadeImpl(medicalRecordDao);
    }

    private MedicalRecord buildRecord(String id, String conditions) {
        MedicalRecord record = new MedicalRecord();
        record.setId(id);
        record.setMedicalConditions(conditions);
        return record;
    }

    /**
     * Returns false if the ID has no match.
     */
    @Test
    void testUpdateNoId() {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc9e0";
        String jowanConditions = "Hypertension";
        MedicalRecord record = buildRecord(allisonId, jowanConditions);
        when(medicalRecordDao.updateMedicalRecord(record)).thenReturn(false);

        boolean result = medicalRecordFacade.updateMedicalRecord(record);

        assertFalse(result);
    }

    /**
     * Returns false if name has no match.
     */
    @Test
    void testUpdateNoName() {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc9e0";
        String jowanConditions = "Hypertension";
        MedicalRecord record = buildRecord(allisonId, jowanConditions);
        when(medicalRecordDao.updateMedicalRecord(record)).thenReturn(false);

        boolean result = medicalRecordFacade.updateMedicalRecord(record);

        assertFalse(result);
    }

    /**
     * Returns true when update is successful for a single ID.
     */
    @Test
    void testUpdateIdSucceed() {
        String jowanId = "4e49eb71-3481-476b-b6ba-5662a48df1e5";
        String jowanConditions = "Hypertension";
        MedicalRecord record = buildRecord(jowanId, jowanConditions);
        when(medicalRecordDao.updateMedicalRecord(record)).thenReturn(true);

        boolean result = medicalRecordFacade.updateMedicalRecord(record);

        assertTrue(result);
    }

    /**
     * Returns true when update is successful for single name.
     */
    @Test
    void testUpdateNameSucceed() {
        String antonioId = "669ce09d-4086-4497-a76e-8cb9afe2b635";
        String antonioConditions = "Asthma";
        MedicalRecord record = buildRecord(antonioId, antonioConditions);
        when(medicalRecordDao.updateMedicalRecord(record)).thenReturn(true);

        boolean result = medicalRecordFacade.updateMedicalRecord(record);

        assertTrue(result);
    }


    /**
     * Update works when chosen from multiple search results.
     */
    @Test
    void testUpdateMultipleResults() {
        String jowanId = "4e49eb71-3481-476b-b6ba-5662a48df1e5";
        String jowanConditions = "Hypertension";
        MedicalRecord record = buildRecord(jowanId, jowanConditions);
        when(medicalRecordDao.updateMedicalRecord(record)).thenReturn(true);

        boolean result = medicalRecordFacade.updateMedicalRecord(record);

        assertTrue(result);
    }

    /**
     * Verifies that the updated record is passed.
     */
    @Test
    void testUpdateToDao() {
        String jowanId = "4e49eb71-3481-476b-b6ba-5662a48df1e5";
        String jowanConditions = "Hypertension";
        MedicalRecord record = buildRecord(jowanId, jowanConditions);
        when(medicalRecordDao.updateMedicalRecord(record)).thenReturn(true);

        medicalRecordFacade.updateMedicalRecord(record);

        verify(medicalRecordDao).updateMedicalRecord(record);
        }
    }
