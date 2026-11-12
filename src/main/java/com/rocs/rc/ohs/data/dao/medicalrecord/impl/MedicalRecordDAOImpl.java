package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDAO;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of MedicalRecordDAO for Oracle database.
 */
public class MedicalRecordDAOImpl implements MedicalRecordDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MedicalRecordDTO> search(String keyword) {
        List<MedicalRecordDTO> records = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return records;
        }

        String pattern = "%" + keyword.trim() + "%";

        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(QueryConstants.SEARCH_MEDICAL_RECORD)) {

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            ps.setString(5, pattern);
            ps.setString(6, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapToDTO(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search medical records", e);
        }

        return records;
    }

    /**
     * Maps the current row of the ResultSet to a MedicalRecordDTO.
     *
     * @param rs the ResultSet positioned at the current row
     * @return the mapped MedicalRecordDTO
     * @throws SQLException if a database access error occurs
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
        dto.setConsultationDate(date != null ? date.toLocalDateTime() : null);

        return dto;
    }
}
