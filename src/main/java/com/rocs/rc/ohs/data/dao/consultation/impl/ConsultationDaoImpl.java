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

    /**
     * Saves a new consultation.
     *
     * @param consultation The consultation to save. Its ID must be set.
     * @return The saved {@link Consultation}.
     */
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
            statement.executeUpdate();
            return consultation;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving the consultation.", ex);
        }
    }

    /**
     * Retrieves the consultations of a patient, latest first.
     *
     * @param patientId The UUID of the patient.
     * @return A list of consultations of the patient.
     */
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
        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Error retrieving the consultations.", ex);
        }
        return consultations;
    }

    /**
     * Maps one consultation row to a {@link Consultation}.
     *
     * @param resultSet The query result holding the consultation.
     * @param patientId The UUID of the patient.
     * @return The {@link Consultation} built from the row.
     * @throws SQLException If a column cannot be read.
     */
    private Consultation toConsultation(ResultSet resultSet, String patientId)
            throws SQLException {
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
        consultation.setConsultationDate(
                resultSet.getTimestamp("consultation_date").toLocalDateTime());
        consultation.setComplaint(resultSet.getString("complaint"));
        consultation.setDiagnosis(resultSet.getString("diagnosis"));
        consultation.setTreatment(resultSet.getString("treatment"));
        consultation.setActionTaken(resultSet.getString("action_taken"));
        consultation.setRemarks(resultSet.getString("remarks"));
        return consultation;
    }

    /**
     * Converts a database value to its record type.
     *
     * @param value The database value of the record type.
     * @return The matching {@link RecordType}.
     */
    private RecordType toRecordType(String value) {
        for (RecordType type : RecordType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalStateException("Unknown record type: " + value);
    }
}
