package com.rocs.rc.ohs.data.dao.filter.impl;


import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Start of test
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FilterDaoImplTest {

    /*
     * database connection.
     */
    @Mock
    private Connection connection;

    /*
     * prepared statement.
     */
    @Mock
    private PreparedStatement statement;

    /*
     * result set.
     */
    @Mock
    private ResultSet resultSet;

    /*
     * static connection helper.
     */
    private MockedStatic<ConnectionHelper> connectionHelper;

    /*
     * Data access layer of test
     */
    private FilterDaoImpl filterDao;

    /*
     * Starting the test if error.
     */
    @BeforeEach
    void setUp() throws SQLException {
        connectionHelper = mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        filterDao = new FilterDaoImpl();
    }

    /*
     * Testing.
     */
    @AfterEach
    void tearDown() {
        connectionHelper.close();
    }

    /*
     * It back in database.
     * back in SQL.
     */
    private String capturedSql() throws SQLException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(connection).prepareStatement(captor.capture());
        return captor.getValue();
    }

    /*
     * If you have no criteria  given they no filter found.
     */
    @Test
    void filterRecordsNoCriteria()
            throws SQLException {
        filterDao.filterMedicalRecords(null, null, null);

        assertFalse(capturedSql().contains(" ? "));
        verify(statement, never()).setString(anyInt(), anyString());
    }

    /*
     * If patient matched in search have firstname and lastname.
     */
    @Test
    void filterFirstLastName()
            throws SQLException {
        filterDao.filterMedicalRecords("jowan", null, null);

        String sql = capturedSql();
        assertTrue(sql.contains("lower(p.first_name) like ?"));
        assertTrue(sql.contains("lower(p.last_name) like ?"));
        verify(statement).setString(1, "%jowan%");
        verify(statement).setString(2, "%jowan%");
    }

    /*
     * If the patient matched in medical condition.
     */
    @Test
    void filterRecordsByCondition() throws SQLException {
        filterDao.filterMedicalRecords(null, "Asthma", null);

        assertTrue(capturedSql()
                .contains("lower(m.medical_conditions) like ?"));
        verify(statement).setString(1, "%asthma%");
    }

    /*
     * When search in allergies have matched in column.
     */
    @Test
    void filterRecordsByAllergy() throws SQLException {
        filterDao.filterMedicalRecords(null, null, "Rashes");

        assertTrue(capturedSql().contains("lower(m.allergies) like ?"));
        verify(statement).setString(1, "%rashes%");
    }

    /*
     * If they ignore or empty.
     */
    @Test
    void noMatchReturnsEmpty() {
        List<MedicalRecord> records =
                filterDao.filterMedicalRecords("None", null, null);

        assertTrue(records.isEmpty());
    }

    /*
     * IF they matched in medical record of patient.
     * throws message.
     */
    @Test
    void rowToRecord() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("record-1");
        when(resultSet.getString("patient_id")).thenReturn("patient-1");
        when(resultSet.getString("first_name")).thenReturn("Jowan");
        when(resultSet.getString("middle_name")).thenReturn("Carl");
        when(resultSet.getString("last_name")).thenReturn("Quirona");
        when(resultSet.getString("medical_conditions")).thenReturn("Asthma");
        when(resultSet.getString("allergies")).thenReturn(null);

        List<MedicalRecord> records =
                filterDao.filterMedicalRecords("jowan", null, null);

        assertEquals(1, records.size());
        MedicalRecord record = records.getFirst();
        assertEquals("record-1", record.getId());
        assertEquals("patient-1", record.getPatient().getId());
        assertEquals("Jowan", record.getPatient().getFirstName());
        assertEquals("Carl", record.getPatient().getMiddleName());
        assertEquals("Quirona", record.getPatient().getLastName());
        assertEquals("Asthma", record.getMedicalConditions());
    }

    /*
     * Matched in all filter that click of user.
     * throws report message.
     */
    @Test
    void findAllMatches() throws SQLException {
        when(resultSet.next()).thenReturn(true, true, false);

        List<MedicalRecord> records =
                filterDao.filterMedicalRecords(null, null, null);

        assertEquals(2, records.size());
    }

    /*
     * A database error returns an empty list instead of throwing.
     * throws NullException message.
     */
    @Test
    void sqlExceptionEmpty()
            throws SQLException {
        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("None"));

        List<MedicalRecord> records =
                filterDao.filterMedicalRecords("jowan", null, null);

        assertTrue(records.isEmpty());
    }
}