package com.rocs.rc.ohs.data.dao.prescription.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.app.model.personnel.Personnel;
import com.rocs.rc.ohs.app.model.prescription.Prescription;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.prescription.PrescriptionDao;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the data access operations for prescriptions.
 */
public class PrescriptionDaoImpl implements PrescriptionDao {

    /**
     * Saves a new prescription using the given connection.
     *
     * @param connection The connection to use. It is not closed here.
     * @param prescription The prescription to save. Its ID must be set.
     * @return The saved {@link Prescription}.
     * @throws IllegalArgumentException If a required value is missing.
     */
    @Override
    public Prescription add(Connection connection,
                            Prescription prescription) {
        validate(prescription);
        try (PreparedStatement statement = connection.prepareStatement(
                QueryConstants.ADD_PRESCRIPTION_STATEMENT)) {
            statement.setString(1, prescription.getId());
            statement.setString(2, prescription.getConsultation().getId());
            statement.setString(3, prescription.getMedicineStock().getId());
            statement.setString(4, prescription.getPersonnel().getId());
            statement.setInt(5, prescription.getQuantityToDispense());
            statement.setString(6, prescription.getDose());
            statement.setString(7, prescription.getFrequency());
            statement.setString(8, prescription.getInstructions());
            statement.executeUpdate();
            return prescription;
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving the prescription.", ex);
        }
    }

    /**
     * Retrieves all prescriptions issued in a consultation, with the name
     * and unit of each medicine.
     *
     * @param consultationId The UUID of the consultation.
     * @return A list of prescriptions of the consultation.
     */
    @Override
    public List<Prescription> findByConsultationId(String consultationId) {
        List<Prescription> prescriptions = new ArrayList<>();
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants
                             .GET_PRESCRIPTIONS_BY_CONSULTATION_STATEMENT)) {
            statement.setString(1, consultationId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    prescriptions.add(
                            toPrescription(resultSet, consultationId));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Error retrieving the prescriptions.", ex);
        }
        return prescriptions;
    }

    /**
     * Checks that the prescription has everything the insert needs.
     *
     * @param prescription The prescription to check.
     * @throws IllegalArgumentException If a required value is missing.
     */
    private void validate(Prescription prescription) {
        if (prescription == null
                || prescription.getId() == null
                || prescription.getConsultation() == null
                || prescription.getMedicineStock() == null
                || prescription.getPersonnel() == null) {
            throw new IllegalArgumentException("The prescription, its ID, "
                    + "consultation, medicine stock and personnel are "
                    + "required.");
        }
    }

    /**
     * Builds a prescription from the current row of the result set.
     *
     * @param resultSet The result set positioned on a prescription row.
     * @param consultationId The UUID of the consultation.
     * @return The {@link Prescription} built from the row.
     * @throws SQLException If a column cannot be read.
     */
    private Prescription toPrescription(ResultSet resultSet,
                                        String consultationId)
            throws SQLException {
        Consultation consultation = new Consultation();
        consultation.setId(consultationId);
        Medicine medicine = new Medicine();
        medicine.setMedicineName(resultSet.getString("medicine_name"));
        medicine.setUnit(resultSet.getString("unit"));
        MedicineStock medicineStock = new MedicineStock();
        medicineStock.setId(resultSet.getString("medicine_stock_id"));
        medicineStock.setMedicine(medicine);
        Personnel personnel = new Personnel();
        personnel.setId(resultSet.getString("personnel_id"));
        Prescription prescription = new Prescription();
        prescription.setId(resultSet.getString("id"));
        prescription.setConsultation(consultation);
        prescription.setMedicineStock(medicineStock);
        prescription.setPersonnel(personnel);
        prescription.setQuantityToDispense(
                resultSet.getInt("quantity_to_dispense"));
        prescription.setDose(resultSet.getString("dose"));
        prescription.setFrequency(resultSet.getString("frequency"));
        prescription.setInstructions(resultSet.getString("instructions"));
        return prescription;
    }
}
