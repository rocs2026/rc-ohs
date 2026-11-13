
package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedRecordDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles database operations for medical records.
 */
public class MedRecordDaoImpl implements MedRecordDao {

    private static final Logger LOGGER =
            Logger.getLogger(MedRecordDaoImpl.class.getName());

    private static final String SELECT_BY_ID =
            "select id, patient_id, consultation_id, medical_conditions, "
                    + "allergies from medical_record where id = ?";

    private static final String SELECT_BY_PATIENT =
            "select id, patient_id, consultation_id, medical_conditions, "
                    + "allergies from medical_record where patient_id = ?";

    private static final String INSERT_RECORD =
            "insert into medical_record (id, patient_id, consultation_id, "
                    + "medical_conditions, allergies) values (?, ?, ?, ?, ?)";

    /**
     * Finds a medical record using its ID.
     *
     * @param id the medical record ID
     * @return the matching record, or null if not found
     */
    @Override
    public MedicalRecord findById(String id) {
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return toMedicalRecord(rs);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Could not retrieve medical record with ID: " + id, e);
        }

        return null;
    }

    /**
     * Gets all medical records linked to a patient.
     *
     * @param patientId the patient's ID
     * @return a list of the patient's medical records
     */
    @Override
    public List<MedicalRecord> findByPatientId(String patientId) {
        List<MedicalRecord> records = new ArrayList<>();

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_PATIENT)) {

            stmt.setString(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(toMedicalRecord(rs));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Could not retrieve records for patient: " + patientId, e);
        }

        return records;
    }

    /**
     * Saves a medical record in the database.
     *
     * @param medicalRecord the record to save
     * @return true if one record was inserted, otherwise false
     */
    @Override
    public boolean addMedicalRecord(MedicalRecord medicalRecord) {
        if (medicalRecord == null
                || medicalRecord.getPatient() == null
                || medicalRecord.getPatient().getId() == null) {
            return false;
        }

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_RECORD)) {

            stmt.setString(1, medicalRecord.getId());
            stmt.setString(2, medicalRecord.getPatient().getId());

            Consultation consultation = medicalRecord.getConsultation();

            if (consultation == null) {
                stmt.setNull(3, Types.VARCHAR);
            } else {
                stmt.setString(3, consultation.getId());
            }

            stmt.setString(4, medicalRecord.getMedicalConditions());
            stmt.setString(5, medicalRecord.getAllergies());

            return stmt.executeUpdate() == 1;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Failed to save the medical record.", e);
            return false;
        }
    }

    /**
     * Converts a database row into a MedicalRecord object.
     *
     * @param rs the current result set row
     * @return the mapped medical record
     * @throws SQLException if a database column cannot be read
     */
    private MedicalRecord toMedicalRecord(ResultSet rs) throws SQLException {
        MedicalRecord record = new MedicalRecord();
        record.setId(rs.getString("id"));

        Person patient = new Person();
        patient.setId(rs.getString("patient_id"));
        record.setPatient(patient);

        String consultationId = rs.getString("consultation_id");

        if (consultationId != null) {
            Consultation consultation = new Consultation();
            consultation.setId(consultationId);
            record.setConsultation(consultation);
        }

        record.setMedicalConditions(rs.getString("medical_conditions"));
        record.setAllergies(rs.getString("allergies"));

        return record;
    }
}
