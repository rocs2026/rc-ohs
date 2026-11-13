
package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicalrecord.AddMedRecordDao;
import com.rocs.rc.ohs.data.utils.QueryConstants;

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
 * Handles saving and retrieving medical records.
 */
public class AddMedRecordDaoImpl implements AddMedRecordDao {

    /**
     * Logger used to record database errors.
     */
    private static final Logger LOGGER =
            Logger.getLogger(AddMedRecordDaoImpl.class.getName());

    /**
     * Finds a medical record using its ID.
     *
     * @param id the record ID
     * @return the medical record, or null if not found
     */
    @Override
    public MedicalRecord findById(String id) {

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants.GET_MEDICAL_RECORD_BY_ID_STATEMENT)) {

            statement.setString(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return toMedicalRecord(result);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Unable to find medical record with ID: " + id, e);
        }

        return null;
    }

    /**
     * Retrieves all medical records belonging to a patient.
     *
     * @param patientId the patient's ID
     * @return a list of the patient's medical records
     */
    @Override
    public List<MedicalRecord> findByPatientId(String patientId) {
        List<MedicalRecord> records = new ArrayList<>();

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants.GET_MEDICAL_RECORDS_BY_PATIENT_STATEMENT)) {

            statement.setString(1, patientId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    records.add(toMedicalRecord(result));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Unable to get medical records for patient: "
                            + patientId, e);
        }

        return records;
    }

    /**
     * Saves a medical record to the database.
     *
     * @param medicalRecord the medical record to save
     * @return true if the record was saved successfully, otherwise false
     */
    @Override
    public boolean addMedicalRecord(MedicalRecord medicalRecord) {

        if (medicalRecord == null || medicalRecord.getPatient() == null
                || medicalRecord.getPatient().getId() == null) {
            return false;
        }

        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants.ADD_MEDICAL_RECORD_STATEMENT)) {

            statement.setString(1, medicalRecord.getId());
            statement.setString(2, medicalRecord.getPatient().getId());

            Consultation consultation = medicalRecord.getConsultation();

            if (consultation == null) {
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(3, consultation.getId());
            }

            statement.setString(4, medicalRecord.getMedicalConditions());
            statement.setString(5, medicalRecord.getAllergies());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Unable to save medical record.", e);
            return false;
        }
    }

    /**
     * Converts a database result into a MedicalRecord object.
     *
     * @param result the current database result
     * @return the mapped medical record
     * @throws SQLException if a database value cannot be read
     */
    private MedicalRecord toMedicalRecord(
            ResultSet result) throws SQLException {

        MedicalRecord record = new MedicalRecord();
        record.setId(result.getString("id"));

        Person patient = new Person();
        patient.setId(result.getString("patient_id"));
        record.setPatient(patient);

        String consultationId = result.getString("consultation_id");

        if (consultationId != null) {
            Consultation consultation = new Consultation();
            consultation.setId(consultationId);
            record.setConsultation(consultation);
        }

        record.setMedicalConditions(
                result.getString("medical_conditions"));
        record.setAllergies(result.getString("allergies"));

        return record;
    }
}
