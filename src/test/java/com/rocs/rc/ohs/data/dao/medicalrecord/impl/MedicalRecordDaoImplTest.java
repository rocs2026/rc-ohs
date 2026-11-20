
package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the database operations of MedicalRecordDaoImpl.
 * Tests the {@link MedicalRecordDaoImpl}.
 */
class MedicalRecordDaoImplTest {

    /**
     * Mocks the static ConnectionHelper class.
     */
    private MockedStatic<ConnectionHelper> helper;

    /**
     * Mock database connection.
     */
    private Connection connection;

    /**
     * Mock SQL statement used to execute queries.
     */
    private PreparedStatement statement;

    /**
     * Mock result set containing database query results.
     */
    private ResultSet resultSet;

    /**
     * DAO being tested.
     */
    private MedicalRecordDao dao;
    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement;

    private MedicalRecordDaoImpl medicalRecordDao;

    /**
     * Creates the mock objects before each test.
     *
     * @throws SQLException if setting up the mock database fails
     */
    @BeforeEach
    void setUp() throws SQLException {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        helper = mockStatic(ConnectionHelper.class);
        helper.when(ConnectionHelper::getConnection).thenReturn(connection);

        dao = new MedicalRecordDaoImpl();
    }

    /**
     * Closes the static mock after each test.
     */
    @AfterEach
    void tearDown() {
        helper.close();
    }

    /**
     * Tests if the search method correctly retrieves and maps
     * patient medical record information.
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
     * @throws SQLException if reading the mock result set fails
     */
    @Test
    void testSearchValidRecord() throws SQLException {
        LocalDateTime date = LocalDateTime.of(2026, 10, 20, 9, 30);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("p_id")).thenReturn("p1");
        when(resultSet.getString("first_name")).thenReturn("Jowan");
        when(resultSet.getString("middle_name")).thenReturn("A");
        when(resultSet.getString("last_name")).thenReturn("Quirona");
        when(resultSet.getString("mr_id")).thenReturn("mr1");
        when(resultSet.getString("medical_conditions")).thenReturn("Asthma");
        when(resultSet.getString("allergies")).thenReturn("Peanut allergy");
        when(resultSet.getString("c_id")).thenReturn("c1");
        when(resultSet.getTimestamp("consultation_date"))
                .thenReturn(Timestamp.valueOf(date));
        when(resultSet.getString("complaint")).thenReturn("Cough");
        when(resultSet.getString("diagnosis")).thenReturn("Infection");
        when(resultSet.getString("treatment")).thenReturn("Antibiotics");

        List<MedicalRecordDTO> results = dao.search("Jowan");

        assertEquals(1, results.size());

        MedicalRecordDTO dto = results.get(0);
        assertEquals("p1", dto.getPatientId());
        assertEquals("Jowan", dto.getFirstName());
        assertEquals("Quirona", dto.getLastName());
        assertEquals("mr1", dto.getMedicalRecordId());
        assertEquals("Asthma", dto.getMedicalConditions());
        assertEquals("Peanut allergy", dto.getAllergies());
        assertEquals("Cough", dto.getComplaint());
        assertEquals("Infection", dto.getDiagnosis());
        assertEquals("Antibiotics", dto.getTreatment());
        assertEquals(date, dto.getConsultationDate());
    void testUpdateNoId() throws SQLException {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc90";
        String jowanConditions = "Hypertension";
        when(statement.executeUpdate()).thenReturn(0);
        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(allisonId, jowanConditions));
        assertFalse(result);
    }

    /**
     * Tests if the search method handles records without a consultation.
     *
     * @throws SQLException if reading the mock result set fails
     */
    @Test
    void testSearchWithoutConsultation() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("p_id")).thenReturn("p1");
        when(resultSet.getString("mr_id")).thenReturn("mr1");
        when(resultSet.getString("c_id")).thenReturn(null);

        List<MedicalRecordDTO> results = dao.search("Jowan");
    void testUpdateWithNullName() throws SQLException {
        String allisonId = "ea67a306-d343-4847-b204-c92f2d4cc9e0";
        String jowanConditions = "Hypertension";
        when(statement.executeUpdate()).thenReturn(0);

        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(allisonId, jowanConditions));

        assertEquals(1, results.size());
        assertNull(results.get(0).getConsultationDate());
    }

    /**
     * Tests if extra spaces are removed from the search keyword.
     *
     * @throws SQLException if verifying the SQL parameters fails
     */
    @Test
    void testSearchTrimsKeyword() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        dao.search("  Quirona  ");
    void testUpdateWithSingleResult() throws SQLException {
        String jowanId = "4e49eb71-3481-476b-b6ba-5662a48df1e5";
        String jowanConditions = "Hypertension";
        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(jowanId, jowanConditions));

        for (int i = 1; i <= 6; i++) {
            verify(statement).setString(i, "%Quirona%");
        }
    }

    /**
     * Tests if the search method returns an empty list
     * when no matching records are found.
     *
     * @throws SQLException if executing the mock query fails
     */
    @Test
    void testSearchNoResults() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        List<MedicalRecordDTO> results = dao.search("Unknown");
    void testUpdateUsingName() throws SQLException {
        String antonioId = "669ce09d-4086-4497-a76e-8cb9afe2b635";
        String antonioConditions = "Asthma";
        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(antonioId, antonioConditions));

        assertTrue(results.isEmpty());
    }

    /**
     * Tests if null or blank keywords return an empty list
     * without accessing the database.
     */
    @Test
    void testSearchBlankInput() {
        assertTrue(dao.search(null).isEmpty());
        assertTrue(dao.search("   ").isEmpty());

        try {
            verify(connection, never()).prepareStatement(anyString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests if a database error is handled by throwing a runtime exception.
     *
     * @throws SQLException if configuring the mock query fails
     */
    @Test
    void testSearchSqlException() throws SQLException {
        when(statement.executeQuery())
                .thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class,
                () -> dao.search("Jowan"));
    }
    void testUpdateMultipleResults() throws SQLException {
        String jowanId = "4e49b71-3481-476b-b6ba-5662a48df1e5";
        String jowanConditions = "Hypertension";
        when(statement.executeUpdate()).thenReturn(1);

        boolean result = medicalRecordDao.updateMedicalRecord(createMedicalRecord(jowanId, jowanConditions));

        assertTrue(result);
    }

}
