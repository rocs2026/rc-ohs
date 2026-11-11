
package com.rocs.rc.ohs.data.dao.search.impl;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
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
import com.rocs.rc.ohs.data.dao.search.SearchDAO;

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
 * Tests the patient search and medical record operations
 * of SearchDAOImpl using JUnit and Mockito.
 */
class SearchDAOImplTest {

    /**
     * Number of parameters used by the patient search query.
     */
    private static final int SEARCH_PARAMETER_COUNT = 4;

    private MockedStatic<ConnectionHelper> helper;
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private SearchDAO dao;

    /**
     * Sets up mocked database objects before each test.
     *
     * @throws SQLException when the database operation fails.
     */
    @BeforeEach
    void setUp() throws SQLException {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(resultSet);

        helper = mockStatic(ConnectionHelper.class);
        helper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);

        dao = new SearchDAOImpl();
    }

    /**
     * Closes the static mock after each test.
     */
    @AfterEach
    void tearDown() {
        helper.close();
    }

    /**
     * Tests that search returns a matching patient.
     *
     * @throws SQLException if database operation failed.
     */
    @Test
    void searchReturnsMatchingPatient() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("p1");
        when(resultSet.getString("first_name")).thenReturn("Jowan");
        when(resultSet.getString("middle_name")).thenReturn("A");
        when(resultSet.getString("last_name")).thenReturn("Quirona");

        List<Person> patients = dao.search("Jowan");

        assertEquals(1, patients.size());
        assertEquals("p1", patients.get(0).getId());
        assertEquals("Jowan", patients.get(0).getFirstName());
        assertEquals("A", patients.get(0).getMiddleName());
        assertEquals("Quirona", patients.get(0).getLastName());
    }

    /**
     * Tests that search binds the trimmed keyword to all parameters.
     *
     * @throws SQLException only if database operation fails.
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
     * Tests that search returns an empty list when no patients match.
     *
     * @throws SQLException if the database operation fails.
     */
    @Test
    void searchReturnsEmptyListWhenNoMatch() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        List<Person> patients = dao.search("Unknown");

        assertEquals(0, patients.size());
    }

    /**
     * Tests that blank keywords do not access the database.
     */
    @Test
    void searchSkipsDatabaseWhenKeywordBlank() {
        List<Person> nullResult = dao.search(null);
        List<Person> blankResult = dao.search("   ");

        assertEquals(0, nullResult.size());
        assertEquals(0, blankResult.size());

        helper.verify(ConnectionHelper::getConnection, never());
    }

    /**
     * Tests that search throws a runtime exception when the query fails.
     *
     * @throws SQLException only when mocked database operation fails.
     */
    @Test
    void searchThrowsWhenDatabaseFails() throws SQLException {
        when(statement.executeQuery())
                .thenThrow(new SQLException("Database error"));

        assertThrows(
                RuntimeException.class,
                () -> dao.search("Jowan"));
    }

    /**
     * Tests that medical record retrieval includes consultation details.
     *
     * @throws SQLException if a database operation fails.
     */
    @Test
    void viewMedicalRecordReturnsRecordWithConsultation()
            throws SQLException {

        LocalDateTime date = LocalDateTime.of(
                2026, 10, 20, 9, 30);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("mr_id")).thenReturn("mr1");
        when(resultSet.getString("medical_conditions"))
                .thenReturn("Asthma");
        when(resultSet.getString("allergies"))
                .thenReturn("Peanut allergy");
        when(resultSet.getString("c_id")).thenReturn("c1");
        when(resultSet.getTimestamp("consultation_date"))
                .thenReturn(Timestamp.valueOf(date));
        when(resultSet.getString("complaint")).thenReturn("Cough");
        when(resultSet.getString("diagnosis"))
                .thenReturn("Infection");
        when(resultSet.getString("treatment"))
                .thenReturn("Antibiotics");

        List<MedicalRecord> records =
                dao.viewMedicalRecord("p1");

        verify(statement).setString(1, "p1");

        assertEquals(1, records.size());
        assertEquals("mr1", records.get(0).getId());
        assertEquals(
                "Asthma",
                records.get(0).getMedicalConditions());
        assertEquals(
                "Peanut allergy",
                records.get(0).getAllergies());

        assertEquals(
                "c1",
                records.get(0).getConsultation().getId());
        assertEquals(
                date,
                records.get(0).getConsultation()
                        .getConsultationDate());
    }

    /**
     * Tests that a medical record can exist without a consultation.
     *
     * @throws SQLException only if the database operation fails.
     */
    @Test
    void viewMedicalRecordHandlesMissingConsultation()
            throws SQLException {

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("mr_id")).thenReturn("mr1");
        when(resultSet.getString("medical_conditions"))
                .thenReturn("Asthma");
        when(resultSet.getString("allergies"))
                .thenReturn("Peanut allergy");
        when(resultSet.getString("c_id")).thenReturn(null);

        List<MedicalRecord> records =
                dao.viewMedicalRecord("p1");

        assertEquals(1, records.size());
        assertNull(records.get(0).getConsultation());
    }

    /**
     * Tests that medical record retrieval returns an empty list
     * when the patient has no records.
     *
     * @throws SQLException happens when database operation fails.
     */
    @Test
    void viewMedicalRecordReturnsEmptyListWhenNoRows()
            throws SQLException {

        when(resultSet.next()).thenReturn(false);

        List<MedicalRecord> records =
                dao.viewMedicalRecord("p1");

        assertEquals(0, records.size());
    }

    /**
     * Tests that medical record retrieval throws an exception
     * when the database query fails.
     *
     * @throws SQLException only happens when mocked database operation fails.
     */
    @Test
    void viewMedicalRecordThrowsWhenDatabaseFails()
            throws SQLException {

        when(statement.executeQuery())
                .thenThrow(new SQLException("Database error"));

        assertThrows(
                RuntimeException.class,
                () -> dao.viewMedicalRecord("p1"));
    }
}
