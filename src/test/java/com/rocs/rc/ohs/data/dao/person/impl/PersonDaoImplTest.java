package com.rocs.rc.ohs.data.dao.person.impl;

import com.rocs.rc.ohs.app.model.enums.gender.Gender;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link PersonDaoImpl} class.
 */
class PersonDaoImplTest {

    /** The ID of the person used in tests. */
    private static final String PERSON_ID =
            "f04cac48-889d-4a62-9a2b-a274406ece20";

    /** Static connection helper used in tests. */
    private MockedStatic<ConnectionHelper> connectionHelper;

    /** Prepared statement for finding persons. */
    private PreparedStatement findPersons;

    /** Result set containing person information. */
    private ResultSet personResult;

    /** DAO instance being tested. */
    private PersonDaoImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        Connection connection = mock(Connection.class);
        findPersons = mock(PreparedStatement.class);
        personResult = mock(ResultSet.class);

        when(connection.prepareStatement(startsWith("select")))
                .thenReturn(findPersons);
        when(findPersons.executeQuery()).thenReturn(personResult);

        connectionHelper = mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);

        dao = new PersonDaoImpl();
    }

    @AfterEach
    void tearDown() {
        connectionHelper.close();
    }

    @Test
    void findByIdMapsPersonAndHandlesMissingDataAndQueryFailures()
            throws SQLException {
        when(personResult.next()).thenReturn(true, true, false);
        stubPersonRow();

        Timestamp birthDate = Timestamp.valueOf(
                LocalDateTime.of(2005, 3, 14, 0, 0));

        when(personResult.getTimestamp("date_of_birth"))
                .thenReturn(birthDate, null);
        when(personResult.getString("gender"))
                .thenReturn("Male", null);

        Person person = dao.findById(PERSON_ID);

        assertNotNull(person);
        assertEquals(PERSON_ID, person.getId());
        assertEquals("Jowan", person.getFirstName());
        assertEquals("Carl", person.getMiddleName());
        assertEquals("Quirona", person.getLastName());
        assertEquals(Gender.MALE, person.getGender());
        assertEquals(LocalDateTime.of(2005, 3, 14, 0, 0),
                person.getDateOfBirth());
        assertEquals("09171234501", person.getContactNumber());

        verify(findPersons).setString(1, PERSON_ID);

        Person personWithMissingFields = dao.findById(PERSON_ID);

        assertNotNull(personWithMissingFields);
        assertNull(personWithMissingFields.getDateOfBirth());
        assertNull(personWithMissingFields.getGender());

        assertNull(dao.findById("no-such-id"));

        doThrow(new SQLException("failed"))
                .when(findPersons).executeQuery();

        assertNull(dao.findById(PERSON_ID));
    }

    @Test
    void findByNameReturnsMatchesOrAnEmptyListWhenNoMatchOrQueryFails()
            throws SQLException {
        when(personResult.next()).thenReturn(true, true, false, false);
        stubPersonRow();

        List<Person> persons = dao.findByName(" Jowan ");

        assertEquals(2, persons.size());
        assertEquals("Quirona", persons.get(0).getLastName());

        verify(findPersons).setString(1, "%jowan%");
        verify(findPersons).setString(2, "%jowan%");

        assertTrue(dao.findByName("Nobody").isEmpty());

        doThrow(new SQLException("failed"))
                .when(findPersons).executeQuery();

        assertTrue(dao.findByName("Jowan").isEmpty());
    }

    @Test
    void findByNameDoesNotConnectForBlankInput() {
        assertTrue(dao.findByName(null).isEmpty());
        assertTrue(dao.findByName("   ").isEmpty());

        connectionHelper.verify(ConnectionHelper::getConnection, never());
    }

    private void stubPersonRow() throws SQLException {
        when(personResult.getString("id")).thenReturn(PERSON_ID);
        when(personResult.getString("first_name")).thenReturn("Jowan");
        when(personResult.getString("middle_name")).thenReturn("Carl");
        when(personResult.getString("last_name")).thenReturn("Quirona");
        when(personResult.getString("email"))
                .thenReturn("jowan123@rocs.edu.ph");
        when(personResult.getTimestamp("date_of_birth"))
                .thenReturn(Timestamp.valueOf(
                        LocalDateTime.of(2005, 3, 14, 0, 0)));
        when(personResult.getString("gender")).thenReturn("Male");
        when(personResult.getString("address")).thenReturn("Langkaan");
        when(personResult.getString("contact_number"))
                .thenReturn("09171234501");
    }
}
