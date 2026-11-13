package com.rocs.rc.ohs.data.dao.medicine.impl;

import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.app.model.medicine.MedicineCategory;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicine.MedicineDao;
import com.rocs.rc.ohs.data.utils.medicine.QueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * it is the implementation of medicine data access.
 */
public class MedicineDaoImpl implements MedicineDao {

    /**
     * it is the method to get all medicines.
     *
     * @return a list of medicines
     */
    @Override
    public List<Medicine> getAllMedicine() {
        List<Medicine> medicineList = new ArrayList<>();

        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     QueryConstants.SELECT_ALL_MEDICINE);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                medicineList.add(mapToMedicine(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve medicines", e);
        }

        return medicineList;
    }

    /**
     * it is the method to find medicine by id.
     *
     * @param id the medicine id
     * @return the medicine object
     */
    @Override
    public Medicine getMedicineById(String id) {
        Medicine foundMedicine = null;

        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     QueryConstants.SELECT_MEDICINE_BY_ID)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    foundMedicine = mapToMedicine(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve medicine by ID", e);
        }

        return foundMedicine;
    }

    /**
     * it is the method to search medicine.
     *
     * @param keyword the keyword
     * @return a list of matching medicines
     */
    @Override
    public List<Medicine> searchMedicine(String keyword) {
        List<Medicine> searchResults = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return searchResults;
        }

        String pattern = "%" + keyword.trim() + "%";

        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     QueryConstants.SEARCH_MEDICINE)) {

            statement.setString(1, pattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    searchResults.add(mapToMedicine(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to search medicines", e);
        }

        return searchResults;
    }

    /**
     * it is the helper method to map result set.
     *
     * @experimental
     * @param resultSet the result set
     * @return the mapped medicine
     * @throws SQLException if sql error occurs
     */
    private Medicine mapToMedicine(ResultSet resultSet)
            throws SQLException {
        Medicine medicine = new Medicine();
        medicine.setId(resultSet.getString("id"));
        medicine.setMedicineName(
                resultSet.getString("medicine_name"));
        medicine.setUnit(resultSet.getString("unit"));

        MedicineCategory medicineCategory =
                new MedicineCategory();
        medicineCategory.setId(
                resultSet.getString("category_id"));
        medicineCategory.setCategoryName(
                resultSet.getString("category_name"));

        medicine.setCategory(medicineCategory);

        return medicine;
    }
}
