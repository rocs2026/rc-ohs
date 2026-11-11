package com.rocs.rc.ohs.data.dao.search.impl;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.search.SearchDAO;
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
 * Tests SearchDAOImpl methods by mocking database connections and result sets.
 */
public class SearchDAOImplTest {

    // Number of parameters bound in the SQL query for patient search
    private static final int SEARCH_PARAMETER_COUNT = 4;

    private MockedStatic<ConnectionHelper> helper;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private SearchDAO dao;

    /**
     * Mocks the ConnectionHelper and JDBC objects prior to running each test case.
     */
    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        helper = mockStatic(ConnectionHelper.class);
        helper.when(ConnectionHelper::getConnection).thenReturn(connection);
        dao = new SearchDAOImpl();
    }

    /**
     * Releases static mocks after each execution to prevent leakage across tests.
     */
    @AfterEach
    void tearDown() {
        helper.close();
    }

    /**
     * Test Case 1: Verifies that a matching record in the result set is mapped correctly
     * into a Person entity with matching ID, first name, and last name.
     */
    @Test
    void searchReturnsMatchingPatient() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("p1");
        when(resultSet.getString("first_name")).thenReturn("Jowan");
        when(resultSet.getString("last_name")).thenReturn("Quirona");

        List<Person> results = dao.search("Quirona");

        assertEquals(1, results.size());
        assertEquals("p1", results.get(0).getId());
        assertEquals("Jowan", results.get(0).getFirstName());
        assertEquals("Quirona", results.get(0).getLastName());
    }

    /**
     * Test Case 2: Checks that whitespace is stripped from the search term and wrapped
     * in wildcard symbols (%) for all query parameters.
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
     * Test Case 3: Ensures an empty collection is returned when no database rows match
     * the search filter.
     */
    @Test
    void searchReturnsEmptyListWhenNoMatch() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        assertTrue(dao.search("zant ").isEmpty());
    }

    /**
     * Test Case 4: Confirms that passing null or whitespace-only strings short-circuits execution
     * and avoids querying the database.
     */
    @Test
    void searchSkipsDatabaseWhenKeywordBlank() {
        assertTrue(dao.search(null).isEmpty());
        assertTrue(dao.search("   ").isEmpty());

        helper.verify(ConnectionHelper::getConnection, never());
    }

    /**
     * Test Case 5: Verifies that low-level SQLExceptions during search queries get caught
     * and rethrown as unchecked RuntimeExceptions.
     */
    @Test
    void searchThrowsWhenDatabaseFails() throws SQLException {
        when(statement.executeQuery()).thenThrow(new SQLException("down"));

        assertThrows(RuntimeException.class, () -> dao.search("Quirona"));
    }

    /**
     * Test Case 6: Tests mapping of medical record details along with its nested consultation object
     * and date parsing when fetching records for a patient.
     */
    @Test
    void viewMedicalRecordReturnsRecordWithConsultation() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("mr_id")).thenReturn("r1");
        when(resultSet.getString("medical_conditions")).thenReturn("Asthma");
        when(resultSet.getString("allergies")).thenReturn("Peanuts");
        when(resultSet.getString("c_id")).thenReturn("c1");
        when(resultSet.getTimestamp("consultation_date"))
                .thenReturn(Timestamp.valueOf("2026-10-20 09:30:00"));
        when(resultSet.getString("complaint")).thenReturn("Cough");
        when(resultSet.getString("diagnosis")).thenReturn("Infection");
        when(resultSet.getString("treatment")).thenReturn("Antibiotics");

        List<MedicalRecord> records = dao.viewMedicalRecord("p1");

        verify(statement).setString(1, "p1");
        assertEquals(1, records.size());
        assertEquals("Asthma", records.get(0).getMedicalConditions());
        assertEquals("Peanuts", records.get(0).getAllergies());
        assertEquals("Cough", records.get(0).getConsultation().getComplaint());
        assertEquals(LocalDateTime.parse("2026-10-20T09:30:00"),
                records.get(0).getConsultation().getConsultationDate());
    }

    /**
     * Test Case 7: Confirms that a MedicalRecord instance is returned with a null consultation
     * field if no consultation ID was found in the database.
     */
    @Test
    void viewMedicalRecordHandlesMissingConsultation() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("mr_id")).thenReturn("r1");

        List<MedicalRecord> records = dao.viewMedicalRecord("p1");

        assertEquals(1, records.size());
        assertNull(records.get(0).getConsultation());
    }

    /**
     * Test Case 8: Validates that an empty list is returned when looking up medical records
     * for a patient ID that has no stored entries.
     */
    @Test
    void viewMedicalRecordReturnsEmptyListWhenNoRows() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        assertTrue(dao.viewMedicalRecord("p1").isEmpty());
    }

    /**
     * Test Case 9: Ensures SQLExceptions encountered during medical record retrieval are
     * wrapped and thrown as RuntimeExceptions.
     */
    @Test
    void viewMedicalRecordThrowsWhenDatabaseFails() throws SQLException {
        when(statement.executeQuery()).thenThrow(new SQLException("down"));

        assertThrows(RuntimeException.class, () -> dao.viewMedicalRecord("p1"));
    }
}