package com.rocs.rc.ohs.data.dao.consultation.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.enums.recordtype.RecordType;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.app.model.personnel.Personnel;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link ConsultationDaoImpl} class without a real database.
 */
public class ConsultationDaoImplTest {

    private static final String JOWAN_ID = "f04cac48-889d-4a62-9a2b-a274406ece20";

    private static final String ANTONIO_ID = "6e00132d-76c0-4c86-82c2-53ba0211aaf0";

    private static final String STAFF_ID = "64ffaffe-79b5-4690-bbd4-587507b6859c";

    private static final String NEW_ID = "0bd75b66-0afb-4901-88d6-c1e851432727";

    private static final LocalDateTime DATE =
            LocalDateTime.of(2026, 10, 20, 9, 30);

    private static final String DIAGNOSIS = "Bacterial throat infection";

    private static final String TREATMENT = "Antibiotic course and rest";

    private MockedStatic<ConnectionHelper> connectionHelper;

    private Connection connection;

    private PreparedStatement statement;

    private ResultSet resultSet;

    private ConsultationDaoImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(statement.executeUpdate()).thenReturn(1);

        connectionHelper = mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);

        dao = new ConsultationDaoImpl();
    }

    @AfterEach
    void tearDown() {
        connectionHelper.close();
    }

    /**
     * Test save consultation.
     * @throws SQLException
     */
    @Test
    void testSaveConsultation() throws SQLException {
        Consultation consultation = newConsultation(ANTONIO_ID, "Mild dehydration", "Drink plenty of water and rest");

        dao.add(consultation);

        verify(statement).setString(2, ANTONIO_ID);
        verify(statement).setString(7, "Mild dehydration");
        verify(statement).setString(8, "Drink plenty of water and rest");
        verify(statement).executeUpdate();
    }

    /**
     * Test saving with complete details.
     * @throws SQLException
     */
    @Test
    void testSaveWithDetails() throws SQLException {
        Consultation saved = dao.add(newConsultation(JOWAN_ID, DIAGNOSIS, TREATMENT));

        assertEquals(NEW_ID, saved.getId());
        verify(statement).setString(1, NEW_ID);
        verify(statement).setString(2, JOWAN_ID);
        verify(statement).setString(3, STAFF_ID);
        verify(statement).setString(4, RecordType.CHECK_UP.getValue());
        verify(statement).setTimestamp(5, Timestamp.valueOf(DATE));
        verify(statement).setString(7, DIAGNOSIS);
        verify(statement).setString(8, TREATMENT);
        verify(statement).executeUpdate();
    }

    /**
     * Test find consultations of a patient.
     * @throws SQLException
     */
    @Test
    void testFindConsultations() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("staff_id")).thenReturn(STAFF_ID);
        when(resultSet.getString("record_type"))
                .thenReturn(RecordType.CHECK_UP.getValue());
        when(resultSet.getTimestamp("consultation_date"))
                .thenReturn(Timestamp.valueOf(DATE));
        when(resultSet.getString("diagnosis")).thenReturn(DIAGNOSIS);
        when(resultSet.getString("treatment")).thenReturn(TREATMENT);

        List<Consultation> result = dao.findByPatientId(JOWAN_ID);

        verify(statement).setString(1, JOWAN_ID);
        assertEquals(1, result.size());
        assertEquals(DIAGNOSIS, result.get(0).getDiagnosis());
        assertEquals(TREATMENT, result.get(0).getTreatment());
    }

    /**
     * Test find patient by name.
     * @throws SQLException
     */
    @Test
    void testFindPatientByName() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn(JOWAN_ID);
        when(resultSet.getString("first_name")).thenReturn("Jowan");
        when(resultSet.getString("last_name")).thenReturn("Quirona");

        List<Person> result = dao.findPatientsByName("Jowan", "Quirona");

        verify(statement).setString(1, "Jowan");
        verify(statement).setString(2, "Quirona");
        assertEquals(JOWAN_ID, result.get(0).getId());
    }

    /**
     * Test find staff by username.
     * @throws SQLException
     */
    @Test
    void testFindStaffByUsername() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn(STAFF_ID);

        List<Personnel> result = dao.findStaffByUsername("staff");

        verify(statement).setString(1, "staff");
        assertEquals(STAFF_ID, result.get(0).getId());
    }

    private Consultation newConsultation(String patientId, String diagnosis,
                                         String treatment) {
        Person patient = new Person();
        patient.setId(patientId);
        Personnel staff = new Personnel();
        staff.setId(STAFF_ID);
        Consultation consultation = new Consultation();
        consultation.setId(NEW_ID);
        consultation.setPatient(patient);
        consultation.setStaff(staff);
        consultation.setRecordType(RecordType.CHECK_UP);
        consultation.setConsultationDate(DATE);
        consultation.setDiagnosis(diagnosis);
        consultation.setTreatment(treatment);
        return consultation;
    }
}
