package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDAO;
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
 * Search behavior tests for {@link MedicalRecordDAOImpl}.
 */
class MedicalRecordDAOImplTest {

    /** Static connection factory mock. */
    private MockedStatic<ConnectionHelper> helper;

    /** Prepared statement stubbed for search queries. */
    private PreparedStatement statement;

    /** Result set stubbed with mock patient rows. */
    private ResultSet resultSet;

    /** DAO instance under test. */
    private MedicalRecordDAO dao;

    /**
     * Stubs JDBC objects and static connection lookup before each run.
     *
     * @throws SQLException if stubbing fails
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

        dao = new MedicalRecordDAOImpl();
    }

    /**
     * Releases the static connection mock after each run.
     */
    @AfterEach
    void tearDown() {
        helper.close();
    }

    /**
     * Maps a full patient row including medical record and consultation fields.
     *
     * @throws SQLException if result set reading fails
     */
    @Test
    void searchReturnsMatchingRecord() throws SQLException {
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

        List<MedicalRecordDTO> records = dao.search("Jowan");

        assertEquals(1, records.size());
        assertEquals("p1", records.get(0).getPatientId());
        assertEquals("Jowan", records.get(0).getFirstName());
        assertEquals("Quirona", records.get(0).getLastName());
        assertEquals("mr1", records.get(0).getMedicalRecordId());
        assertEquals("Asthma", records.get(0).getMedicalConditions());
        assertEquals("Peanut allergy", records.get(0).getAllergies());
        assertEquals("Cough", records.get(0).getComplaint());
        assertEquals("Infection", records.get(0).getDiagnosis());
        assertEquals("Antibiotics", records.get(0).getTreatment());
        assertEquals(date, records.get(0).getConsultationDate());
    }

    /**
     * Leaves consultation date null when no consultation ID is present.
     *
     * @throws SQLException if result set reading fails
     */
    @Test
    void searchHandlesMissingConsultation() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("p_id")).thenReturn("p1");
        when(resultSet.getString("mr_id")).thenReturn("mr1");
        when(resultSet.getString("c_id")).thenReturn(null);

        List<MedicalRecordDTO> records = dao.search("Jowan");

        assertEquals(1, records.size());
        assertNull(records.get(0).getConsultationDate());
    }

    /**
     * Strips leading and trailing spaces before binding wildcard parameters.
     *
     * @throws SQLException if parameter binding fails
     */
    @Test
    void searchBindsTrimmedKeyword() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        dao.search("  Quirona  ");

        int parameterCount = 6;
        for (int i = 1; i <= parameterCount; i++) {
            verify(statement).setString(i, "%Quirona%");
        }
    }

    /**
     * Returns an empty list when the query matches zero rows.
     *
     * @throws SQLException if query execution fails
     */
    @Test
    void searchReturnsEmptyWhenNoMatch() throws SQLException {
        when(resultSet.next()).thenReturn(false);

        assertEquals(0, dao.search("Unknown").size());
    }

    /**
     * Exits early without opening a connection on null or whitespace input.
     */
    @Test
    void searchSkipsBlankKeyword() {
        assertEquals(0, dao.search(null).size());
        assertEquals(0, dao.search("   ").size());

        helper.verify(ConnectionHelper::getConnection, never());
    }

    /**
     * Propagates SQL failures as a runtimeexception}.
     *
     * @throws SQLException when stubbing query execution failure
     */
    @Test
    void searchThrowsOnDatabaseError() throws SQLException {
        when(statement.executeQuery())
                .thenThrow(new SQLException("Database error"));

        assertThrows(RuntimeException.class, () -> dao.search("Jowan"));
    }
}
