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
 * JDBC implementation of {@link PrescriptionDao}.
 */
public class PrescriptionDaoImpl implements PrescriptionDao {

    @Override
    public Prescription add(Prescription prescription) {
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants.ADD_PRESCRIPTION_STATEMENT)) {
            statement.setString(1, prescription.getId());
            statement.setString(2, prescription.getConsultation().getId());
            statement.setString(3, prescription.getMedicineStock().getId());
            statement.setString(4, prescription.getPersonnel().getId());
            statement.setInt(5, prescription.getQuantityToDispense());
            statement.setString(6, prescription.getDose());
            statement.setString(7, prescription.getFrequency());
            statement.setString(8, prescription.getInstructions());
            int rows = statement.executeUpdate();
            if (rows < 1) {
                System.out.println("No prescription row was saved.");
            }
        } catch (SQLException ex) {
            System.out.println(
                    "Could not save prescription: " + ex.getMessage());
        }
        return prescription;
    }

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
            System.out.println(
                    "Could not read prescriptions: " + ex.getMessage());
        }
        return prescriptions;
    }

    @Override
    public List<MedicineStock> findStocksByMedicineName(String medicineName) {
        List<MedicineStock> stocks = new ArrayList<>();
        try (Connection connection = ConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     QueryConstants
                             .FIND_STOCK_BY_MEDICINE_NAME_STATEMENT)) {
            statement.setString(1, medicineName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stocks.add(toMedicineStock(resultSet));
                }
            }
        } catch (SQLException ex) {
            System.out.println(
                    "Could not find the medicine: " + ex.getMessage());
        }
        return stocks;
    }

    private MedicineStock toMedicineStock(ResultSet resultSet)
            throws SQLException {
        Medicine medicine = new Medicine();
        medicine.setMedicineName(resultSet.getString("medicine_name"));
        medicine.setUnit(resultSet.getString("unit"));
        MedicineStock medicineStock = new MedicineStock();
        medicineStock.setId(resultSet.getString("id"));
        medicineStock.setBatchNumber(resultSet.getString("batch_number"));
        medicineStock.setMedicine(medicine);
        return medicineStock;
    }

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
