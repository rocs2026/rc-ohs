
package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * Tests the {@link MedicalRecordDaoImpl} class.
 */
@ExtendWith(MockitoExtension.class)
class MedicalRecordDaoImplTest {

    /** ID used for medical record test cases. */
    private static final String RECORD_ID =
            "4e49eb71-3481-476b-b6ba-5662a48df1e5";

    /** ID used for patient test cases. */
    private static final String PATIENT_ID =
            "ea67a306-d343-4847-b204-c92f2d4cc9e0";

    /** ID used for consultation test cases. */
    private static final String CONSULTATION_ID =
            "a451a504-a3d4-4721-bf8d-a18b8c199356";

    /** Mock database connection. */
    @Mock
    private Connection connection;

    /** Mock prepared statement for update tests. */
    @Mock
    private PreparedStatement statement;

    /** DAO instance being tested. */
    private MedicalRecordDaoImpl medicalRecordDao;

    /** Prepared statement for medical record queries. */
    private PreparedStatement findRecords;

    /** Prepared statement for inserting medical records. */
    private PreparedStatement insertRecord;

    /** Mock result set returned by database queries. */
    private ResultSet recordResult;

    @BeforeEach
    void setUp() throws SQLException {
        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        medicalRecordDao = new MedicalRecordDaoImpl(connection);

        findRecords = mock(PreparedStatement.class);
        insertRecord = mock(PreparedStatement.class);
        recordResult = mock(ResultSet.class);
    }

    /**
     * Creates a record for update tests.
     *
     * @param id the record ID.
     * @param conditions the medical conditions.
     * @return the prepared record.
     */
    private MedicalRecord buildRecord(String id, String conditions) {
        MedicalRecord record = new MedicalRecord();
        record.setId(id);
        record.setMedicalConditions(conditions);
        return record;
    }

    /**
     * Creates a record with patient and optional consultation details.
     *
     * @param id the record ID.
     * @param consultationId the consultation ID, if available.
     * @return the prepared record.
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
     * Sets up the values returned by the mocked result set.
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

    @Test
    void updateReturnsFalseWhenIdIsNotFound() throws SQLException {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc90";
        String jowanConditions = "Hypertension";

        when(statement.executeUpdate()).thenReturn(0);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(allisonId, jowanConditions));

        assertFalse(result);
    }

    @Test
    void updateReturnsFalseWhenNoRecordMatches() throws SQLException {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc9e0";
        String jowanConditions = "Hypertension";

        when(statement.executeUpdate()).thenReturn(0);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(allisonId, jowanConditions));

        assertFalse(result);
    }

    @Test
    void updateReturnsTrueForOneUpdatedRecord() throws SQLException {
        String jowanId = RECORD_ID;
        String jowanConditions = "Hypertension";

        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(jowanId, jowanConditions));

        assertTrue(result);
    }

    @Test
    void updateReturnsTrueForAnotherMatchingRecord() throws SQLException {
        String antonioId = "669ce09d-4086-4497-a76e-8cb9afe2b635";
        String antonioConditions = "Asthma";

        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(antonioId, antonioConditions));

        assertTrue(result);
    }

    @Test
    void updateWorksForASelectedSearchResult() throws SQLException {
        String jowanId = "4e49b71-3481-476b-b6ba-5662a48df1e5";
        String jowanConditions = "Hypertension";

        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(
                buildRecord(jowanId, jowanConditions));

        assertTrue(result);
    }

    @Test
    void updateUsesTheCorrectValues() throws SQLException {
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

    @Test
    void addWorksWithAndWithoutConsultation() throws SQLException {
        when(connection.prepareStatement(
                QueryConstants.ADD_MEDICAL_RECORD_STATEMENT))
                .thenReturn(insertRecord);

        when(insertRecord.executeUpdate()).thenReturn(1);

        MedicalRecord existingRecord =
                newMedicalRecord(RECORD_ID, CONSULTATION_ID);

        assertTrue(medicalRecordDao.addMedicalRecord(existingRecord));

        MedicalRecord newRecord = newMedicalRecord(RECORD_ID, null);

        assertTrue(medicalRecordDao.addMedicalRecord(newRecord));

        verify(insertRecord, times(2)).setString(1, RECORD_ID);
        verify(insertRecord, times(2)).setString(2, PATIENT_ID);
        verify(insertRecord).setString(3, CONSULTATION_ID);
        verify(insertRecord, times(2)).setString(4, "Asthma");
        verify(insertRecord, times(2)).setString(5, "Peanuts");
        verify(insertRecord).setNull(3, Types.VARCHAR);
    }

    @Test
    void addRejectsInvalidRecordsAndFailedInserts() throws SQLException {
        MedicalRecord noPatient = newMedicalRecord(RECORD_ID, null);
        noPatient.setPatient(null);

        MedicalRecord noPatientId = newMedicalRecord(RECORD_ID, null);
        noPatientId.getPatient().setId(null);

        assertFalse(medicalRecordDao.addMedicalRecord(null));
        assertFalse(medicalRecordDao.addMedicalRecord(noPatient));
        assertFalse(medicalRecordDao.addMedicalRecord(noPatientId));

        verify(connection, never()).prepareStatement(
                QueryConstants.ADD_MEDICAL_RECORD_STATEMENT);

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

    @Test
    void findByPatientNameReturnsMatchingRecords() throws SQLException {
        String patientName = "Juan Dela Cruz";
        String searchPattern = "%juan dela cruz%";

        when(connection.prepareStatement(
                QueryConstants.GET_MEDICAL_RECORDS_BY_PATIENT_STATEMENT))
                .thenReturn(findRecords);

        when(findRecords.executeQuery()).thenReturn(recordResult);
        when(recordResult.next()).thenReturn(true, true, false);

        stubRow(CONSULTATION_ID);

        List<MedicalRecord> records =
                medicalRecordDao.findByPatientName(patientName);

        assertEquals(2, records.size());
        assertEquals(PATIENT_ID, records.get(0).getPatient().getId());
        assertEquals(PATIENT_ID, records.get(1).getPatient().getId());

        verify(findRecords).setString(1, searchPattern);
    }

    @Test
    void findByPatientNameReturnsEmptyListWhenNoRecordsExist()
            throws SQLException {

        when(connection.prepareStatement(
                QueryConstants.GET_MEDICAL_RECORDS_BY_PATIENT_STATEMENT))
                .thenReturn(findRecords);

        when(findRecords.executeQuery()).thenReturn(recordResult);
        when(recordResult.next()).thenReturn(false);

        List<MedicalRecord> records =
                medicalRecordDao.findByPatientName("Juan Dela Cruz");

        assertTrue(records.isEmpty());
    }

    @Test
    void findByPatientNameReturnsEmptyListWhenQueryFails()
            throws SQLException {

        when(connection.prepareStatement(
                QueryConstants.GET_MEDICAL_RECORDS_BY_PATIENT_STATEMENT))
                .thenReturn(findRecords);

        doThrow(new SQLException("failed"))
                .when(findRecords).executeQuery();

        List<MedicalRecord> records =
                medicalRecordDao.findByPatientName("Juan Dela Cruz");

        assertTrue(records.isEmpty());
    }

    @Test
    void findByPatientNameReturnsEmptyListWhenNameIsBlank()
            throws SQLException {

        List<MedicalRecord> records =
                medicalRecordDao.findByPatientName(" ");

        assertTrue(records.isEmpty());

        verify(connection, never()).prepareStatement(
                QueryConstants.GET_MEDICAL_RECORDS_BY_PATIENT_STATEMENT);
    }
}
