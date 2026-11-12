
package com.rocs.rc.ohs.data.dao.medicine.impl;

import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.app.model.medicine.MedicineCategory;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicine.MedicineDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the MedicineDao interface for managing medicine records.
 */
public class MedicineDaoImpl implements MedicineDao {

    /**
     * Connection used for all database operations.
     */
    private Connection connection;

    /**
     * Establishes the database connection.
     */
    public MedicineDaoImpl() {
        try {
            connection = ConnectionHelper.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to establish database connection", e);
        }
    }

    /**
     * Retrieves all medicine records from the database.
     *
     * @return a list of all recorded medicines, or an empty list if none exist.
     */
    @Override
    public List<Medicine> getAllMedicine() {
        List<Medicine> medicineList = new ArrayList<>();

        String sql = "SELECT medicine.id, medicine.medicine_name, "
                + "medicine.unit, medicine.category_id, "
                + "medicine_category.category_name "
                + "FROM medicine "
                + "LEFT JOIN medicine_category "
                + "ON medicine.category_id = medicine_category.id "
                + "ORDER BY medicine.medicine_name;";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Medicine newMedicine = new Medicine();
                newMedicine.setId(resultSet.getString("id"));
                newMedicine.setMedicineName(
                        resultSet.getString("medicine_name"));
                newMedicine.setUnit(resultSet.getString("unit"));

                MedicineCategory medicineCategory =
                        new MedicineCategory();
                medicineCategory.setId(
                        resultSet.getString("category_id"));
                medicineCategory.setCategoryName(
                        resultSet.getString("category_name"));

                newMedicine.setCategory(medicineCategory);
                medicineList.add(newMedicine);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve medicines", e);
        }

        return medicineList;
    }

    /**
     * Retrieves a specific medicine record by its unique identifier.
     *
     * @param id the unique identifier of the medicine to look up.
     * @return the Medicine object if found, or null if it does not exist.
     */
    @Override
    public Medicine getMedicineById(String id) {
        Medicine foundMedicine = null;

        String sql = "SELECT medicine.id, medicine.medicine_name, "
                + "medicine.unit, medicine.category_id, "
                + "medicine_category.category_name "
                + "FROM medicine "
                + "LEFT JOIN medicine_category "
                + "ON medicine.category_id = medicine_category.id "
                + "WHERE medicine.id = ?;";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    foundMedicine = new Medicine();
                    foundMedicine.setId(resultSet.getString("id"));
                    foundMedicine.setMedicineName(
                            resultSet.getString("medicine_name"));
                    foundMedicine.setUnit(
                            resultSet.getString("unit"));

                    MedicineCategory medicineCategory =
                            new MedicineCategory();
                    medicineCategory.setId(
                            resultSet.getString("category_id"));
                    medicineCategory.setCategoryName(
                            resultSet.getString("category_name"));

                    foundMedicine.setCategory(medicineCategory);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve medicine by ID", e);
        }

        return foundMedicine;
    }

    /**
     * Searches for medicine records matching the specified name or keyword.
     *
     * @param keyword the search keyword or medicine name.
     * @return a list of matching medicine records.
     */
    @Override
    public List<Medicine> searchMedicine(String keyword) {
        List<Medicine> searchResults = new ArrayList<>();

        String sql = "SELECT medicine.id, medicine.medicine_name, "
                + "medicine.unit, medicine.category_id, "
                + "medicine_category.category_name "
                + "FROM medicine "
                + "LEFT JOIN medicine_category "
                + "ON medicine.category_id = medicine_category.id "
                + "WHERE medicine.medicine_name LIKE ? "
                + "ORDER BY medicine.medicine_name;";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, "%" + keyword + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Medicine searchedMedicine = new Medicine();
                    searchedMedicine.setId(
                            resultSet.getString("id"));
                    searchedMedicine.setMedicineName(
                            resultSet.getString("medicine_name"));
                    searchedMedicine.setUnit(
                            resultSet.getString("unit"));

                    MedicineCategory medicineCategory =
                            new MedicineCategory();
                    medicineCategory.setId(
                            resultSet.getString("category_id"));
                    medicineCategory.setCategoryName(
                            resultSet.getString("category_name"));

                    searchedMedicine.setCategory(medicineCategory);
                    searchResults.add(searchedMedicine);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to search medicines", e);
        }

        return searchResults;
    }
}
