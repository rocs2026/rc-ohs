package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.utils.QueryConstants;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the medical record search using a single Oracle query
 * and returns the results as DTOs.
 */
public class MedicalRecordDAOImpl implements MedicalRecordDAO {

    /**
     * The number of keyword parameters used by the search query.
     */
    private static final int SEARCH_PARAMETER_COUNT = 6;

    /**
     * Searches for a patient and returns the patient's medical records.
     *
     * @param keyword the text to search for
     * @return the matching medical records
     */
    @Override
    public List<MedicalRecordDTO> search(String keyword) {
        List<MedicalRecordDTO> records = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return records;
        }

        String pattern = "%" + keyword.trim() + "%";

        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     QueryConstants.SEARCH_MEDICAL_RECORD)) {

            for (int i = 1; i <= SEARCH_PARAMETER_COUNT; i++) {
                ps.setString(i, pattern);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapToDTO(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to search medical records", e);
        }

        return records;
    }

    /**
     * Maps the current database row to a medical record DTO.
     *
     * @param rs the result set positioned at a row
     * @return the mapped medical record DTO
     * @throws SQLException if a database column cannot be read
     */
    private MedicalRecordDTO mapToDTO(ResultSet rs) throws SQLException {
        MedicalRecordDTO dto = new MedicalRecordDTO();

        dto.setPatientId(rs.getString("p_id"));
        dto.setFirstName(rs.getString("first_name"));
        dto.setMiddleName(rs.getString("middle_name"));
        dto.setLastName(rs.getString("last_name"));
        dto.setMedicalRecordId(rs.getString("mr_id"));
        dto.setMedicalConditions(rs.getString("medical_conditions"));
        dto.setAllergies(rs.getString("allergies"));
        dto.setComplaint(rs.getString("complaint"));
        dto.setDiagnosis(rs.getString("diagnosis"));
        dto.setTreatment(rs.getString("treatment"));

        Timestamp date = rs.getTimestamp("consultation_date");

        dto.setConsultationDate(
                date == null ? null : date.toLocalDateTime());

        return dto;
    }
}
