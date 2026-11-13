package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
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
 * JDBC-based implementation for medical record data operations.
 */
public class MedicalRecordDAOImpl implements MedicalRecordDAO {

    private final Connection connection;

    /** Uses the standard connection helper by default. */
    public MedicalRecordDAOImpl() {
        this(openConnection());
    }

    /** Allows passing an explicit connection (useful for testing). */
    public MedicalRecordDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<MedicalRecordDTO> search(String keyword) {
        List<MedicalRecordDTO> records = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return records;
        }

        String pattern = "%" + keyword.trim() + "%";

        try (PreparedStatement ps = connection.prepareStatement(
                QueryConstants.SEARCH_MEDICAL_RECORD)) {

            // Bind search term to all query placeholders
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

    @Override
    public boolean updateMedicalRecord(MedicalRecord medicalRecord) {
        try (PreparedStatement statement = connection
                .prepareStatement(QueryConstants.UPDATE_MEDICAL_RECORD_STATEMENT)) {
            statement.setString(1, medicalRecord.getMedicalConditions());
            statement.setString(2, medicalRecord.getAllergies());
            statement.setString(3, medicalRecord.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update medical record: " + medicalRecord.getId(), e);
        }
    }

    private static Connection openConnection() {
        try {
            return ConnectionHelper.getConnection();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to open database connection", e);
        }
    }

    /** Maps a database result row into a transfer object. */
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
