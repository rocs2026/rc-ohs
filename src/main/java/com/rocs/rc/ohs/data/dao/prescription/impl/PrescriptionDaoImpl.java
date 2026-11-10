package com.rocs.rc.ohs.data.dao.prescription.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.app.model.personnel.Personnel;
import com.rocs.rc.ohs.app.model.prescription.Prescription;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.prescription.PrescriptionDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implements the data access operations for prescriptions.
 */
public class PrescriptionDaoImpl implements PrescriptionDao {

    /** The SQL statement that saves a prescription. */
    private static final String INSERT_SQL =
            "INSERT INTO prescription (id, consultation_id, "
                    + "medicine_stock_id, personnel_id, quantity_to_dispense, "
                    + "dose, frequency, instructions) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    /** The SQL statement that retrieves the prescriptions of a consultation. */
    private static final String FIND_BY_CONSULTATION_SQL =
            "SELECT p.id, p.medicine_stock_id, p.personnel_id, "
                    + "p.quantity_to_dispense, p.dose, p.frequency, "
                    + "p.instructions, m.medicine_name, m.unit "
                    + "FROM prescription p "
                    + "JOIN medicine_stock ms ON ms.id = p.medicine_stock_id "
                    + "JOIN medicine m ON m.id = ms.medicine_id "
                    + "WHERE p.consultation_id = ? ORDER BY p.id";

    /**
     * Saves a new prescription using the given connection.
     *
     * @param connection The connection to use. It is not closed here.
     * @param prescription The prescription to save.
     * @return The saved {@link Prescription} with its generated UUID.
     */
    @Override
    public Prescription add(Connection connection,
                            Prescription prescription) {
        String id = UUID.randomUUID().toString();
        try (PreparedStatement statement =
                     connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, id);
            statement.setString(2, prescription.getConsultation().getId());
            statement.setString(3, prescription.getMedicineStock().getId());
            statement.setString(4, prescription.getPersonnel().getId());
            statement.setInt(5, prescription.getQuantityToDispense());
            statement.setString(6, prescription.getDose());
            statement.setString(7, prescription.getFrequency());
            statement.setString(8, prescription.getInstructions());
            statement.executeUpdate();
            prescription.setId(id);
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
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_CONSULTATION_SQL)) {
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
