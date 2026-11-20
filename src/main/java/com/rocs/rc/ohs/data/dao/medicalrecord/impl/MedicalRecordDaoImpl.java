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
 * dao implementation for handling medical records in the database.
 */
public class MedicalRecordDaoImpl implements MedicalRecordDao {

    /**
     * active database connection instance.
     */
    private final Connection connection;

    /**
     * default constructor that fetches connection from helper.
     */
    public MedicalRecordDaoImpl() {
        this(openConnection());
    }

    /**
     * overloaded constructor used for testing with mock connection.
     *
     * @param conn database connection
     */
    public MedicalRecordDaoImpl(Connection conn) {
        this.connection = conn;
    }

    @Override
    public List<MedicalRecordDTO> search(String keyword) {
        List<MedicalRecordDTO> records = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return records;
        }

        String queryPattern = "%" + keyword.trim() + "%";

        try (PreparedStatement ps = connection.prepareStatement(
                QueryConstants.SEARCH_MEDICAL_RECORD)) {

            final int totalParams = 6;
            for (int i = 1; i <= totalParams; i++) {
                ps.setString(i, queryPattern);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                records.add(mapToDTO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to search medical records", e);
        }

        return records;
    }

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
            String recordId = medicalRecord.getId();
            throw new RuntimeException(
                    "Failed to update medical record: " + recordId, e);
        }
    }

    /**
     * grabs a connection using the connection helper class.
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
     * maps the current resultset row into a medical record dto.
     *
     * @param rs active result set
     * @throws SQLException if column lookup fails
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

        Timestamp consultationTimestamp = rs.getTimestamp("consultation_date");
        dto.setConsultationDate(
                consultationTimestamp != null ? consultationTimestamp.toLocalDateTime() : null);

        return dto;
    }
}
