package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link MedicalRecordDaoImpl} class.
 */
@ExtendWith(MockitoExtension.class)
class MedicalRecordDaoImplTest {

    /** The ID of the medical record used in tests. */
    private static final String RECORD_ID =
            "4e49eb71-3481-476b-b6ba-5662a48df1e5";

    /** The ID of the patient used in tests. */
    private static final String PATIENT_ID =
            "ea67a306-d343-4847-b204-c92f2d4cc9e0";

    /** The ID of the consultation used in tests. */
    private static final String CONSULTATION_ID =
            "a451a504-a3d4-4721-bf8d-a18b8c199356";

    /** Mock connection. */
    @Mock
    private Connection connection;

    /** Mock statement. */
    @Mock
    private PreparedStatement statement;

    /** The DAO under test. */
    private MedicalRecordDaoImpl medicalRecordDao;

    /** Static connection helper used in tests. */
    private MockedStatic<ConnectionHelper> connectionHelper;

    /** Prepared statement for finding medical records. */
    private PreparedStatement findRecords;

    /** Prepared statement for adding a medical record. */
    private PreparedStatement insertRecord;

    /** Result set containing medical record information. */
    private ResultSet recordResult;

    /**
     * Prepares the DAO and mocked database objects before each test.
     *
     * @throws SQLException if the statement cannot be prepared.
     */
    @BeforeEach
    void setUp() throws SQLException {
        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        medicalRecordDao = new MedicalRecordDaoImpl(connection);

        findRecords = mock(PreparedStatement.class);
        insertRecord = mock(PreparedStatement.class);
        recordResult = mock(ResultSet.class);

        when(findRecords.executeQuery()).thenReturn(recordResult);

        connectionHelper = mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);
    }

    /**
     * Closes the mocked static connection helper after each test.
     */
    @AfterEach
    void tearDown() {
        connectionHelper.close();
    }

    /**
     * Builds a medical record with its ID and medical conditions.
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
     * Builds a medical record with patient and consultation information.
     *
     * @param id the ID of the medical record.
     * @param consultationId the ID of the consultation.
     * @return the medical record.
     */
    private MedicalRecord newMedicalRecord(
            String id, String consultationId) {

        Person patient = new Person();
        patient.setId(PATIENT_ID);

        MedicalRecord record = new MedicalRecord();
        record.setId(id);
        record.setPatient(patient);

        if (consultationId != null) {
            Consultation consultation = new Consultation();
            consultation.setId(consultationId);
            record.setConsultation(consultation);
        }

        record.setMedicalConditions("Asthma");
        record.setAllergies("Peanuts");

        return record;
    }

    /**
     * Prepares the mocked result set with medical record information.
     *
     * @param consultationId the consultation ID.
     * @throws SQLException if a database value cannot be read.
     */
    private void stubRow(String consultationId) throws SQLException {
        when(recordResult.getString("id")).thenReturn(RECORD_ID);
        when(recordResult.getString("patient_id")).thenReturn(PATIENT_ID);
        when(recordResult.getString("consultation_id"))
                .thenReturn(consultationId);
        when(recordResult.getString("medical_conditions"))
                .thenReturn("Asthma");
        when(recordResult.getString("allergies")).thenReturn("Peanuts");
    }

    /**
     * TESTCASE1: Should return false if no ID exists.
     *
     * @throws SQLException if the SQL query update fails.
     */
    @Test
    void updateMedicalRecordWhenIdReturnsNothingReturnsFalse()
            throws SQLException {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc90";
        String jowanConditions = "Hypertension";

        when(statement.executeUpdate()).thenReturn(0);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(allisonId, jowanConditions));

        assertFalse(result);
    }

    /**
     * TC2: Returns false if name has no match.
     *
     * @throws SQLException if database connection has an error.
     */
    @Test
    void updateMedicalRecordWhenNameReturnsNothingReturnsFalse()
            throws SQLException {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc9e0";
        String jowanConditions = "Hypertension";

        when(statement.executeUpdate()).thenReturn(0);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(allisonId, jowanConditions));

        assertFalse(result);
    }

    /**
     * TC3: Returns true when update is successful for single ID.
     *
     * @throws SQLException if the update has failed.
     */
    @Test
    void updateMedicalRecordWhenIdReturnsSingleResultReturnsTrue()
            throws SQLException {
        String jowanId = RECORD_ID;
        String jowanConditions = "Hypertension";

        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(jowanId, jowanConditions));

        assertTrue(result);
    }

    /**
     * TC4: Check if update returns true using name parameter.
     *
     * @throws SQLException if SQL parameters are wrong.
     */
    @Test
    void updateMedicalRecordWhenNameReturnsSingleResultReturnsTrue()
            throws SQLException {
        String antonioId = "669ce09d-4086-4497-a76e-8cb9afe2b635";
        String antonioConditions = "Asthma";

        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(antonioId, antonioConditions));

        assertTrue(result);
    }

    /**
     * TC7: Update should work even if chosen from multiple search results.
     *
     * @throws SQLException if table update cannot process data.
     */
    @Test
    void updateMedicalRecordWhenSelectedFromMultipleResultsReturnsTrue()
            throws SQLException {
        String jowanId = "4e49b71-3481-476b-b6ba-5662a48df1e5";
        String jowanConditions = "Hypertension";

        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(jowanId, jowanConditions));

        assertTrue(result);
    }

    /**
     * TC9: Verify that correct values are actually passed to the statement.
     *
     * @throws SQLException if verification fails.
     */
    @Test
    void updateMedicalRecordWhenUpdatedSendsNewValuesToDatabase()
            throws SQLException {
        String jowanId = RECORD_ID;
        String jowanConditions = "Hypertension";

        when(statement.executeUpdate()).thenReturn(1);

        medicalRecordDao.updateMedicalRecord(
                buildRecord(jowanId, jowanConditions));

        verify(statement).setString(1, jowanConditions);
        verify(statement).setString(2, null);
        verify(statement).setString(3, jowanId);
        verify(statement).executeUpdate();
    }

    /**
     * Tests adding records with and without a consultation.
     *
     * @throws SQLException if the database operation fails.
     */
    @Test
    void addMedicalRecordStoresRecordsWithAndWithoutConsultation()
            throws SQLException {

        when(connection.prepareStatement(
                QueryConstants.ADD_MEDICAL_RECORD_STATEMENT))
                .thenReturn(insertRecord);

        when(insertRecord.executeUpdate()).thenReturn(1);

        MedicalRecord existingRecord =
                newMedicalRecord(RECORD_ID, CONSULTATION_ID);

        assertTrue(medicalRecordDao.addMedicalRecord(existingRecord));

        verify(insertRecord).setString(1, RECORD_ID);
        verify(insertRecord).setString(2, PATIENT_ID);
        verify(insertRecord).setString(3, CONSULTATION_ID);
        verify(insertRecord).setString(4, "Asthma");
        verify(insertRecord).setString(5, "Peanuts");

        MedicalRecord newRecord = newMedicalRecord(RECORD_ID, null);

        assertTrue(medicalRecordDao.addMedicalRecord(newRecord));

        verify(insertRecord).setString(1, RECORD_ID);
        verify(insertRecord).setNull(3, Types.VARCHAR);
    }

    /**
     * Tests invalid records and unsuccessful insert operations.
     *
     * @throws SQLException if the database operation fails.
     */
    @Test
    void addMedicalRecordRejectsInvalidOrUnsuccessfulRecords()
            throws SQLException {

        MedicalRecord noPatient = newMedicalRecord(RECORD_ID, null);
        noPatient.setPatient(null);

        MedicalRecord noPatientId = newMedicalRecord(RECORD_ID, null);
        noPatientId.getPatient().setId(null);

        assertFalse(medicalRecordDao.addMedicalRecord(null));
        assertFalse(medicalRecordDao.addMedicalRecord(noPatient));
        assertFalse(medicalRecordDao.addMedicalRecord(noPatientId));

        connectionHelper.verify(ConnectionHelper::getConnection, never());

        when(connection.prepareStatement(
                QueryConstants.ADD_MEDICAL_RECORD_STATEMENT))
                .thenReturn(insertRecord);

        when(insertRecord.executeUpdate()).thenReturn(0);

        assertFalse(medicalRecordDao.addMedicalRecord(
                newMedicalRecord(RECORD_ID, CONSULTATION_ID)));

        doThrow(new SQLException("failed"))
                .when(insertRecord).executeUpdate();

        assertFalse(medicalRecordDao.addMedicalRecord(
                newMedicalRecord(RECORD_ID, CONSULTATION_ID)));
    }

    /**
     * Tests finding a medical record by ID.
     *
     * @throws SQLException if the database operation fails.
     */
    @Test
    void findByIdHandlesFoundMissingAndFailedQueries()
            throws SQLException {

        when(connection.prepareStatement(
                QueryConstants.GET_MEDICAL_RECORD_BY_ID_STATEMENT))
                .thenReturn(findRecords);

        when(recordResult.next()).thenReturn(true, false);
        stubRow(CONSULTATION_ID);

        MedicalRecord record = medicalRecordDao.findById(RECORD_ID);

        assertNotNull(record);
        assertEquals(RECORD_ID, record.getId());
        assertEquals(PATIENT_ID, record.getPatient().getId());
        assertEquals(CONSULTATION_ID,
                record.getConsultation().getId());
        assertEquals("Asthma", record.getMedicalConditions());
        assertEquals("Peanuts", record.getAllergies());

        verify(findRecords).setString(1, RECORD_ID);

        when(recordResult.next()).thenReturn(false);

        assertNull(medicalRecordDao.findById("no-such-id"));

        doThrow(new SQLException("failed"))
                .when(findRecords).executeQuery();

        assertNull(medicalRecordDao.findById(RECORD_ID));
    }

    /**
     * Tests finding all medical records belonging to a patient.
     *
     * @throws SQLException if the database operation fails.
     */
    @Test
    void findByPatientIdReturnsRecordsOrAnEmptyList()
            throws SQLException {

        when(connection.prepareStatement(
                QueryConstants.GET_MEDICAL_RECORDS_BY_PATIENT_STATEMENT))
                .thenReturn(findRecords);

        when(recordResult.next()).thenReturn(true, true, false);
        stubRow(CONSULTATION_ID);

        List<MedicalRecord> records =
                medicalRecordDao.findByPatientId(PATIENT_ID);

        assertEquals(2, records.size());
        assertEquals(PATIENT_ID, records.get(0).getPatient().getId());
        assertEquals(PATIENT_ID, records.get(1).getPatient().getId());

        verify(findRecords).setString(1, PATIENT_ID);
    }
}
