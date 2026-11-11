package com.rocs.rc.ohs.data.dao.search.impl;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;

import com.rocs.rc.ohs.data.dao.search.SearchDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;


public class SearchDAOImpl implements SearchDAO{

    /** The number of keyword parameters used by the search query. */
    private static final int SEARCH_PARAMETER_COUNT = 4;


    @Override
    public List<Person> search(String keyword) {
        List<Person> patients = new ArrayList<>();

        if (keyword==null || keyword.trim().isEmpty()){
            return patients;
        }
        String sql = "Select Distinct p.id, p.first_name, p.middlename, p.last_name FROM person p"
                + "LEFT JOIN student s ON s.person_id = p.id "
                + "WHERE LOWER(p.first_name) LIKE LOWER(?) "
                + "OR LOWER(p.middle_name) LIKE LOWER(?) "
                + "OR LOWER(p.last_name) LIKE LOWER(?) "
                + "OR LOWER(s.student_number) LIKE LOWER(?) "
                + "ORDER BY p.last_name, p.first_name";
        String pattern = "%" + keyword.trim() + "%";

        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 1; i <= SEARCH_PARAMETER_COUNT; i++) {
                ps.setString(i, pattern);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Person person = new Person();
                    person.setId(rs.getString("id"));
                    person.setFirstName(rs.getString("first_name"));
                    person.setMiddleName(rs.getString("middle_name"));
                    person.setLastName(rs.getString("last_name"));
                    patients.add(person);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search patients", e);
        }


        return List.of();
    }

    @Override
    public List<MedicalRecord> viewMedicalRecord(String patientId) {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT mr.id AS mr_id, mr.medical_conditions, "
                + "mr.allergies, c.id AS c_id, c.consultation_date, "
                + "c.complaint, c.diagnosis, c.treatment "
                + "FROM medical_record mr "
                + "LEFT JOIN consultation c ON c.id = mr.consultation_id "
                + "WHERE mr.patient_id = ? "
                + "ORDER BY c.consultation_date DESC";
        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read medical records", e);
        }
        return List.of();
    }


    /**
     * Maps the current row to a medical record and its consultation.
     *
     * @param rs the result set positioned at a row.
     * @return the mapped medical record.
     * @throws SQLException if a column cannot be read.
     */
    private MedicalRecord mapRecord(ResultSet rs) throws SQLException {
        MedicalRecord record = new MedicalRecord();
        record.setId(rs.getString("mr_id"));
        record.setMedicalConditions(rs.getString("medical_conditions"));
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