package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.facade.medicalrecord.impl.MedicalRecordFacadeImpl;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JDBC implementation of {@link MedicalRecordDao}.
 */
public class MedicalRecordDaoImpl implements MedicalRecordDao {

    /** The database connection. */
    private  final Connection connection;

    /**
     * creates dao that open operation.
     */
    public MedicalRecordDaoImpl() {
        this.connection = null;
    }
    /**
     * Create a DAO that uses the given database connection.
     *
     * @param newConnection connection.
     */
    public MedicalRecordDaoImpl(Connection newConnection) {
        this.connection = newConnection;
    }

    /** {@inheritDoc} */
    @Override
    public boolean updateMedicalRecord(MedicalRecord medicalRecord) {
        try {
            if (connection != null) {
                return update(connection, medicalRecord);
            }
            try (Connection newConnection = ConnectionHelper.getConnection()) {
                return update(newConnection, medicalRecord);
            }
        } catch (SQLException e) {
            return false;

        }
    }


    private boolean update(
            Connection connection, MedicalRecord medicalRecord)
        throws SQLException {
            try (PreparedStatement statement = connection.prepareStatement(
                    QueryConstants.UPDATE_MEDICAL_RECORD_STATEMENT)) {
                statement.setString(1, medicalRecord.getMedicalConditions());
                statement.setString(2, medicalRecord.getAllergies());
                statement.setString(3, medicalRecord.getId());
                return statement.executeUpdate() > 0;

            }
        }
    }
