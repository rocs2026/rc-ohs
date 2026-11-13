package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.dto.MedicalRecordDTO;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of MedicalRecordDAO.
 */
public class MedicalRecordDaoImpl implements MedicalRecordDao {

    /**
     * The database connection.
     */
    private final Connection connection;

    /**
     *
     * Creates a DAO that opens its own database connection.
     */
    public MedicalRecordDaoImpl() {
        this(openConnection());
    }

    /**
     * Creates a DAO that uses the given database connection.
     *
     * @param conn the database connection to use
     */
    public MedicalRecordDaoImpl(Connection conn) {
        this.connection = conn;
    }

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

        try (PreparedStatement ps = connection.prepareStatement(
                QueryConstants.SEARCH_MEDICAL_RECORD)) {

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
            throw new RuntimeException(
                    "Failed to search medical records", e);
        }

        return records;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateMedicalRecord(
            MedicalRecord medicalRecord) {
        try (PreparedStatement statement = connection
                .prepareStatement(QueryConstants.
                        UPDATE_MEDICAL_RECORD_STATEMENT)) {
            statement.setString(
                    1, medicalRecord.getMedicalConditions());
            statement.setString(
                    2, medicalRecord.getAllergies());
            statement.setString(
                    3, medicalRecord.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            String id = medicalRecord.getId();
            throw new RuntimeException(
                    "Failed to update medical record: " + id, e);
        }
    }

    /**
     * Opens a database connection through the shared helper.
     *
     * @return the opened database connection
     */
    private static Connection openConnection() {
        try {
            return ConnectionHelper.getConnection();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to open the database connection", e);
        }
    }

    /**
     * Maps the current row of the ResultSet to a MedicalRecordDTO.
     *
     * @param rs the ResultSet positioned at the current row
     * @return the mapped MedicalRecordDTO
     * @throws SQLException if a database access error occurs
     */
    private MedicalRecordDTO mapToDTO(ResultSet rs)
            throws SQLException {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setPatientId(rs.getString("p_id"));
        dto.setFirstName(rs.getString("first_name"));
        dto.setMiddleName(rs.getString("middle_name"));
        dto.setLastName(rs.getString("last_name"));
        dto.setMedicalRecordId(rs.getString("mr_id"));
        dto.setMedicalConditions(
                rs.getString("medical_conditions"));
        dto.setAllergies(rs.getString("allergies"));
        dto.setComplaint(rs.getString("complaint"));
        dto.setDiagnosis(rs.getString("diagnosis"));
        dto.setTreatment(rs.getString("treatment"));

        Timestamp date = rs.getTimestamp("consultation_date");
        dto.setConsultationDate(
                date != null ? date.toLocalDateTime() : null);

        return dto;
    }
}
