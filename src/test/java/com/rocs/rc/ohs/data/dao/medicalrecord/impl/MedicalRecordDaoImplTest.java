package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link MedicalRecordDaoImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class MedicalRecordDaoImplTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement;

    private MedicalRecordDaoImpl medicalRecordDao;

    /**
     * Prepares the DAO and the mocked statement before each test.
     *
     * @throws SQLException if the statement cannot be prepared.
     */
    @BeforeEach
    void setUp() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        medicalRecordDao = new MedicalRecordDaoImpl(connection);
    }

    /**
     * Creates medical record with only its ID and medical condition sets.
     *
     * @param id the ID of the medical record.
     * @param conditions the medical conditions of the patient.
     * @return the medical record.
     */
    private MedicalRecord createMedicalRecord(String id, String conditions) {
        MedicalRecord record = new MedicalRecord();
        record.setId(id);
        record.setMedicalConditions(conditions);
        return record;
    }

    /**
     * TC1: Should return false if no ID exists.
     *
     * @throws SQLException if the sql query update fails
     */
    @Test
    void testUpdateNoId() throws SQLException {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc90";
        String jowanConditions = "Hypertension";
        when(statement.executeUpdate()).thenReturn(0);
        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(allisonId, jowanConditions));
        assertFalse(result);
    }

    /**
     * TC2: Returns false if name has no match.
     *
     * @throws SQLException if database connection has error
     */
    @Test
    void testUpdateWithNullName() throws SQLException {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc9e0";
        String jowanConditions = "Hypertension";
        when(statement.executeUpdate()).thenReturn(0);

        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(allisonId, jowanConditions));

        assertFalse(result);
    }

    /**
     * TC3: Returns true when update is successful for single ID.
     *
     * @throws SQLException if the update has failed
     */
    @Test
    void testUpdateWithSingleResult() throws SQLException {
        String jowanId = "4e49eb71-3481-476b-b6ba-5662a48df1e5";
        String jowanConditions = "Hypertension";
        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(jowanId, jowanConditions));

        assertTrue(result);
    }

    /**
     * TC4: Check if update returns true using  name parameter.
     *
     * @throws SQLException if sql params are wrong.
     */
    @Test
    void testUpdateUsingName() throws SQLException {
        String antonioId = "669ce09d-4086-4497-a76e-8cb9afe2b635";
        String antonioConditions = "Asthma";
        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(antonioId, antonioConditions));

        assertTrue(result);
    }

    /**
     * TC7: Update should work even if chosen from multiple search results.
     *
     * @throws SQLException if table update cannot process data.
     */
    @Test
    void testUpdateMultipleResults() throws SQLException {
        String jowanId = "4e49b71-3481-476b-b6ba-5662a48df1e5";
        String jowanConditions = "Hypertension";
        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(jowanId, jowanConditions));

        assertTrue(result);
    }

}
