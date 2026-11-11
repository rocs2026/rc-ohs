
package com.rocs.rc.ohs.data.dao.medicalRecord.impl;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalRecord.MedicalRecordDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests MedicalRecordDAOImpl using JUnit
 * and Mockito.
 */
class MedicalRecordDAOImplTest {

    /**
     * Number of parameters used by the search query.
     */
    private static final int SEARCH_PARAMETER_COUNT = 4;

    /**
     * Mocks the static methods of ConnectionHelper.
     */
    private MockedStatic<ConnectionHelper> helper;

    /**
     * Mock SQL prepared statement.
     */
    private PreparedStatement statement;

    /**
     * Mock result set containing query results.
     */
    private ResultSet resultSet;

    /**
     * DAO instance under test.
     */
    private MedicalRecordDAO dao;

    /**
     * Sets up mocked database objects before each test.
     *
     * @throws SQLException if the database operation fails.
     */
    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(resultSet);

        helper = mockStatic(ConnectionHelper.class);
        helper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);

        dao = new MedicalRecordDAOImpl();
    }

    /**
     * Closes the static mock after each test.
     */
    @AfterEach
    void tearDown() {
        helper.close();
    }

    /**
     * Tests that a matching patient is returned with the medical record
     * and consultation.
     *
     * @throws SQLException only when the database operation fails.
     */
    @Test
    void searchReturnsPatientWithMedicalRecord() throws SQLException {
        LocalDateTime date = LocalDateTime.of(2026, 10, 20, 9, 30);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("p_id")).thenReturn("p1");
        when(resultSet.getString("first_name")).thenReturn("Jowan");
        when(resultSet.getString("middle_name")).thenReturn("A");
        when(resultSet.getString("last_name")).thenReturn("Quirona");
        when(resultSet.getString("mr_id")).thenReturn("mr1");
        when(resultSet.getString("medical_conditions"))
                .thenReturn("Asthma");
        when(resultSet.getString("allergies"))
                .thenReturn("Peanut allergy");
        when(resultSet.getString("c_id")).thenReturn("c1");
        when(resultSet.getTimestamp("consultation_date"))
                .thenReturn(Timestamp.valueOf(date));
        when(resultSet.getString("complaint")).thenReturn("Cough");
        when(resultSet.getString("diagnosis")).thenReturn("Infection");
        when(resultSet.getString("treatment")).thenReturn("Antibiotics");

        List<MedicalRecord> records = dao.search("Jowan");

        assertEquals(1, records.size());
        assertEquals("p1", records.get(0).getPatient().getId());
        assertEquals("Jowan", records.get(0).getPatient().getFirstName());
        assertEquals("Quirona", records.get(0).getPatient().getLastName());
        assertEquals("mr1", records.get(0).getId());
        assertEquals("Asthma", records.get(0).getMedicalConditions());
        assertEquals("Peanut allergy", records.get(0).getAllergies());
        assertEquals("c1", records.get(0).getConsultation().getId());
        assertEquals("Cough",
                records.get(0).getConsultation().getComplaint());
        assertEquals(date,
                records.get(0).getConsultation().getConsultationDate());
    }

    /**
     * Tests that a patient without a consultation has none.
     *
     * @throws SQLException when database operation fails.
     */
    @Test
    void searchHandlesMissingConsultation() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("p_id")).thenReturn("p1");
        when(resultSet.getString("mr_id")).thenReturn("mr1");
        when(resultSet.getString("c_id")).thenReturn(null);

        List<MedicalRecord> records = dao.search("Jowan");

        assertEquals(1, records.size());
        assertNull(records.get(0).getConsultation());
    }

    /**
     * Tests that the trimmed keyword is bound to all parameters.
     *
     * @throws SQLException if the database operation fails.
     */
    @Test
    void searchBindsTrimmedKeyword() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        dao.search("  Quirona  ");

        for (int i = 1; i <= SEARCH_PARAMETER_COUNT; i++) {
            verify(statement).setString(i, "%Quirona%");
        }
    }

    /**
     * Tests that search returns an empty list when no patient matches.
     *
     * @throws SQLException only if database operation fails.
     */
    @Test
    void searchReturnsEmptyListWhenNoMatch() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        assertEquals(0, dao.search("Unknown").size());
    }

    /**
     * Tests that blank keywords do not access the database.
     */
    @Test
    void searchSkipsDatabaseWhenKeywordBlank() {
        assertEquals(0, dao.search(null).size());
        assertEquals(0, dao.search("   ").size());

        helper.verify(ConnectionHelper::getConnection, never());
    }

    /**
     * Tests that search throws a runtime exception when the query fails.
     *
     * @throws SQLException when the mocked database operation fails.
     */
    @Test
    void searchThrowsWhenDatabaseFails() throws SQLException {
        when(statement.executeQuery())
                .thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> dao.search("Jowan"));
    }
}