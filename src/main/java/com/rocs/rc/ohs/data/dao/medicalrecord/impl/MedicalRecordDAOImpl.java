package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the medical record search using a single Oracle query.
 */
public class MedicalRecordDAOImpl implements MedicalRecordDAO {

    /**
     * The number of keyword parameters used by the search query.
     */
    private static final int SEARCH_PARAMETER_COUNT = 5;

    /**
     * Searches for a patient and returns the patient's medical records.
     *
     * @param keyword the text to search for
     * @return the matching medical records
     */
    @Override
    public List<MedicalRecord> search(String keyword) {
        List<MedicalRecord> records = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return records;
        }

        String sql = "SELECT p.id AS p_id, p.first_name, p.middle_name, "
                + "p.last_name, mr.id AS mr_id, mr.medical_conditions, "
                + "mr.allergies, c.id AS c_id, c.consultation_date, "
                + "c.complaint, c.diagnosis, c.treatment "
                + "FROM person p "
                + "LEFT JOIN student s ON s.person_id = p.id "
                + "JOIN medical_record mr ON mr.patient_id = p.id "
                + "LEFT JOIN consultation c ON c.id = mr.consultation_id "
                + "WHERE LOWER(p.id) LIKE LOWER(?) "
                + "OR LOWER(p.first_name) LIKE LOWER(?) "
                + "OR LOWER(p.middle_name) LIKE LOWER(?) "
                + "OR LOWER(p.last_name) LIKE LOWER(?) "
                + "OR LOWER(s.student_number) LIKE LOWER(?) "
                + "ORDER BY p.last_name, p.first_name, "
                + "c.consultation_date DESC";

        String pattern = "%" + keyword.trim() + "%";

        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 1; i <= SEARCH_PARAMETER_COUNT; i++) {
                ps.setString(i, pattern);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to search medical records", e);
        }

        return records;
    }

    /**
     * Maps the current database row to a medical record with its
     * patient and consultation.
     *
     * @param rs the result set positioned at a row
     * @return the mapped medical record
     * @throws SQLException if a database column cannot be read
     */
    private MedicalRecord mapRecord(ResultSet rs) throws SQLException {
        Person patient = new Person();

        patient.setId(rs.getString("p_id"));
        patient.setFirstName(rs.getString("first_name"));
        patient.setMiddleName(rs.getString("middle_name"));
        patient.setLastName(rs.getString("last_name"));

        MedicalRecord record = new MedicalRecord();

        record.setId(rs.getString("mr_id"));
        record.setPatient(patient);
        record.setMedicalConditions(
                rs.getString("medical_conditions"));
        record.setAllergies(rs.getString("allergies"));

        String consultationId = rs.getString("c_id");

        if (consultationId != null) {
            Consultation consultation = new Consultation();

            consultation.setId(consultationId);

            Timestamp date = rs.getTimestamp("consultation_date");

            consultation.setConsultationDate(
                    date == null ? null : date.toLocalDateTime());

            consultation.setComplaint(rs.getString("complaint"));
            consultation.setDiagnosis(rs.getString("diagnosis"));
            consultation.setTreatment(rs.getString("treatment"));

            record.setConsultation(consultation);
        }

        return record;
    }
}