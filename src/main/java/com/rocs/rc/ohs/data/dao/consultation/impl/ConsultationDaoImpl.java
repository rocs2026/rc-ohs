package com.rocs.rc.ohs.data.dao.consultation.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.enums.recordtype.RecordType;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.app.model.personnel.Personnel;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.consultation.ConsultationDao;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link ConsultationDao}.
 */
public class ConsultationDaoImpl implements ConsultationDao {

    @Override
    public Consultation add(Consultation consultation) {
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants.ADD_CONSULTATION_STATEMENT)) {
            Timestamp date =
                    Timestamp.valueOf(consultation.getConsultationDate());
            statement.setString(1, consultation.getId());
            statement.setString(2, consultation.getPatient().getId());
            statement.setString(3, consultation.getStaff().getId());
            statement.setString(4, consultation.getRecordType().getValue());
            statement.setTimestamp(5, date);
            statement.setString(6, consultation.getComplaint());
            statement.setString(7, consultation.getDiagnosis());
            statement.setString(8, consultation.getTreatment());
            statement.setString(9, consultation.getActionTaken());
            statement.setString(10, consultation.getRemarks());
            int rows = statement.executeUpdate();
            if (rows < 1) {
                System.out.println("No consultation row was saved.");
            }
        } catch (SQLException ex) {
            System.out.println(
                    "Could not save consultation: " + ex.getMessage());
        }
        return consultation;
    }

    @Override
    public List<Consultation> findByPatientId(String patientId) {
        List<Consultation> consultations = new ArrayList<>();
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants.GET_CONSULTATIONS_BY_PATIENT_STATEMENT)) {
            statement.setString(1, patientId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    consultations.add(toConsultation(resultSet, patientId));
                }
            }
        } catch (SQLException | IllegalStateException ex) {
            System.out.println(
                    "Could not read consultations: " + ex.getMessage());
        }
        return consultations;
    }

    @Override
    public List<Person> findPatientsByName(String firstName,
                                           String lastName) {
        List<Person> persons = new ArrayList<>();
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants.FIND_PERSON_BY_NAME_STATEMENT)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Person person = new Person();
                    person.setId(resultSet.getString("id"));
                    person.setFirstName(resultSet.getString("first_name"));
                    person.setMiddleName(resultSet.getString("middle_name"));
                    person.setLastName(resultSet.getString("last_name"));
                    persons.add(person);
                }
            }
        } catch (SQLException ex) {
            System.out.println(
                    "Could not find the patient: " + ex.getMessage());
        }
        return persons;
    }

    @Override
    public List<Personnel> findStaffByUsername(String username) {
        List<Personnel> staff = new ArrayList<>();
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants.FIND_PERSONNEL_BY_USERNAME_STATEMENT)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Personnel personnel = new Personnel();
                    personnel.setId(resultSet.getString("id"));
                    staff.add(personnel);
                }
            }
        } catch (SQLException ex) {
            System.out.println(
                    "Could not find the staff: " + ex.getMessage());
        }
        return staff;
    }

    private Consultation toConsultation(ResultSet resultSet, String patientId)
            throws SQLException, IllegalStateException {
        Person patient = new Person();
        patient.setId(patientId);
        Personnel staff = new Personnel();
        staff.setId(resultSet.getString("staff_id"));
        Consultation consultation = new Consultation();
        consultation.setId(resultSet.getString("id"));
        consultation.setPatient(patient);
        consultation.setStaff(staff);
        consultation.setRecordType(
                toRecordType(resultSet.getString("record_type")));
        consultation.setConsultationDate(resultSet
                .getTimestamp("consultation_date").toLocalDateTime());
        consultation.setComplaint(resultSet.getString("complaint"));
        consultation.setDiagnosis(resultSet.getString("diagnosis"));
        consultation.setTreatment(resultSet.getString("treatment"));
        consultation.setActionTaken(resultSet.getString("action_taken"));
        consultation.setRemarks(resultSet.getString("remarks"));
        return consultation;
    }

    private RecordType toRecordType(String value)
            throws IllegalStateException {
        for (RecordType type : RecordType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalStateException("Unknown record type: " + value);
    }
}
