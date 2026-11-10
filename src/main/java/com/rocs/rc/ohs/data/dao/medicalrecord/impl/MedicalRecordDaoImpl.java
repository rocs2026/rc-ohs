package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JDBC implementation of {@link MedicalRecordDao}.
 */
public class MedicalRecordDaoImpl implements MedicalRecordDao {

    /** SQL statement for updating a medical record. */
    private static final String UPDATE_MEDICAL_RECORD =
            "UPDATE medical_record "
                + "SET medical_conditions = ?, allergies = ? "
                + "WHERE id =?";

    /** The database connection. */
    private  final Connection connection;

    /**
     * Create a DAO that uses the given database connection.
     *
     * @param newConnection the database connection.
     */

    public MedicalRecordDaoImpl(Connection newConnection) {
        this.connection = newConnection;
    }

    /** {@inheritDoc} */
    @Override
    public boolean updateMedicalRecord(MedicalRecord medicalRecord) {
        try (PreparedStatement statement =
                    connection.prepareStatement(UPDATE_MEDICAL_RECORD)) {
            statement.setString(1, medicalRecord.getMedicalConditions());
            statement.setString(2, medicalRecord.getAllergies());
            statement.setString(3, medicalRecord.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw  new RuntimeException(
                    "Failed to update medical record: " + medicalRecord.getId(), e);
        }
    }
}
