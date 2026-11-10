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
 * test the {@link MedicalRecordDaoImpl}.
 */
@ExtendWith(MockitoExtension.class)
public class MedicalRecordDaoImplTest {

    /** The medical record ID of Jowan. */
    private static final String JOWAN_RECORD_ID =
            "4e49eb71-3481-476b-b6ba-5662a48df1e5";

    /** The new medical conditions of Jowan. */
    private static final String JOWAN_NEW_CONDITIONS = "Hypertension";

    /** The record ID of Antonio . */
    private static final String ANTONIO_RECORD_ID =
            "669ce09d-4086-4497-a76e-8cb9afe2b635";

    /** New medical conditions of Antonio. */
    private static final String ANTONIO_NEW_CONDITIONS = "Asthma";

    /** The ID of Allison, has no medical record . */
    private static final String ALLISON_ID =
            "ea67a306-d343-4847-b204-c92f2d4cc9e0";

    /** mock connection. */
    @Mock
    private Connection connection;

    /** mock statement. */
    @Mock
    private PreparedStatement statement;

    /** The DAO under test. */
    private MedicalRecordDaoImpl medicalRecordDao;

    /**
     * Prepares the DAO and the mocked statement before each test.
     *
     * @throws SQLException if the statement cannot be prepared.
     */
    @BeforeEach
    void setUp()
            throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        medicalRecordDao = new MedicalRecordDaoImpl(connection);
    }

    /**
     * Builds medical record with only its ID and medical condition sets.
     *
     * @param id the ID of the medical record.
     * @param conditions the medical conditions of the patient.
     * @return the medical record.
     */
    private MedicalRecord buildRecord(String id, String conditions) {
        MedicalRecord record = new MedicalRecord();
        record.setId(id);
        record.setMedicalConditions(conditions);
        return record;
    }

    /**
     * TESTCASE1: Should return false if no ID exists.
     *
     * @throws SQLException if the sql query update fails
     */
    @Test
    void updateMedicalRecord_whenIdReturnsNothing_returnsFalse()
            throws SQLException {
        when(statement.executeUpdate()).thenReturn(0);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(ALLISON_ID, JOWAN_NEW_CONDITIONS));

        assertFalse(result);
    }

    /**
     * TC2: Returns false if name has no match.
     *
     * @throws SQLException if database connection has error
     */
    @Test
    void updateMedicalRecord_whenNameReturnsNothing_returnsFalse()
            throws SQLException {
        when(statement.executeUpdate()).thenReturn(0);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(ALLISON_ID, JOWAN_NEW_CONDITIONS));

        assertFalse(result);
    }

    /**
     * TC3: Returns true when update is successful for single ID.
     *
     * @throws SQLException if the update has failed
     */
    @Test
    void updateMedicalRecord_whenIdReturnsSingleResult_returnsTrue()
            throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(JOWAN_RECORD_ID, JOWAN_NEW_CONDITIONS));

        assertTrue(result);
    }

    /**
     * TC4: Check if update returns true using  name parameter.
     *
     * @throws SQLException if sql params are wrong.
     */
    @Test
    void updateMedicalRecord_whenNameReturnsSingleResult_returnsTrue()
            throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(ANTONIO_RECORD_ID, ANTONIO_NEW_CONDITIONS));

        assertTrue(result);
    }

    /**
     * TC7: Update should work even if chosen from multiple search results.
     *
     * @throws SQLException if table update cannot process data.
     */
    @Test
    void updateMedicalRecord_whenSelectedFromMultipleResults_returnsTrue()
            throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(JOWAN_RECORD_ID, JOWAN_NEW_CONDITIONS));

        assertTrue(result);
    }

    /**
     * TC9: Verify that corrects values are actually passed to the statement.
     *
     * @throws SQLException if verification drops.
     */
    @Test
    void updateMedicalRecord_whenUpdated_sendsNewValuesToDatabase()
            throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        medicalRecordDao.updateMedicalRecord(
                buildRecord(JOWAN_RECORD_ID, JOWAN_NEW_CONDITIONS));

        verify(statement).setString(1, JOWAN_NEW_CONDITIONS);
        verify(statement).setString(3, JOWAN_RECORD_ID);
        verify(statement).executeUpdate();
    }
}
