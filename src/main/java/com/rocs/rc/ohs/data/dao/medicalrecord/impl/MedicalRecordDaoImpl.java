package com.rocs.rc.ohs.data.dao.medicalrecord.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * JDBC implementation of {@link MedicalRecordDao}.
 */
public class MedicalRecordDaoImpl implements MedicalRecordDao {

    /** The database connection. */
    private final Connection connection;

    /**
     * Create a DAO that uses the given database connection.
     *
     * @param newConnection connection.
     */
    public MedicalRecordDaoImpl(Connection newConnection) {
        this.connection = newConnection;
    }

    /**
     * Retrieves all medical records belonging to a patient by name.
     *
     * @param patientName the patient's name
     * @return a list of the patient's medical records
     */
    @Override
    public List<MedicalRecord> findByPatientName(String patientName) {
        List<MedicalRecord> records = new ArrayList<>();

        if (patientName == null || patientName.trim().isEmpty()) {
            return records;
        }

        String searchPattern =
                "%" + patientName.trim().toLowerCase(Locale.ROOT) + "%";

        try (PreparedStatement statement = connection.prepareStatement(
                QueryConstants.GET_MEDICAL_RECORDS_BY_PATIENT_STATEMENT)) {

            statement.setString(1, searchPattern);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    records.add(toMedicalRecord(result));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

        try (PreparedStatement statement = connection.prepareStatement(
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
            e.printStackTrace();
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
    private MedicalRecord toMedicalRecord(ResultSet result)
            throws SQLException {

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

    /** {@inheritDoc} */
    @Override
    public boolean updateMedicalRecord(
            MedicalRecord medicalRecord) {
        try (PreparedStatement statement = connection
                .prepareStatement(QueryConstants.
                        UPDATE_MEDICAL_RECORD_STATEMENT)) {
            statement.setString(1, medicalRecord.getMedicalConditions());
            statement.setString(2, medicalRecord.getAllergies());
            statement.setString(3, medicalRecord.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            String id = medicalRecord.getId();
            throw new RuntimeException(
                    "Failed to update medical record: " + id, e);
        }
    }
}
