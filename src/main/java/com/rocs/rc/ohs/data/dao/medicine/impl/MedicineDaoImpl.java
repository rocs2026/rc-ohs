package com.rocs.rc.ohs.data.dao.medicine.impl;

import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.app.model.medicine.MedicineCategory;
import com.rocs.rc.ohs.data.connection.MySqlConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicine.MedicineDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the data access operations for medicine records.
 */
public class MedicineDaoImpl implements MedicineDao {

    private Medicine mapResultSetToMedicine(ResultSet resultSet) throws SQLException {
        MedicineCategory category = new MedicineCategory();
        category.setId(resultSet.getString("category_id"));
        category.setCategoryName(resultSet.getString("category_name"));

        Medicine medicine = new Medicine();
        medicine.setId(resultSet.getString("id"));
        medicine.setMedicineName(resultSet.getString("medicine_name"));
        medicine.setUnit(resultSet.getString("unit"));
        medicine.setCategory(category);
        return medicine;
    }

    private List<Medicine> fetchMedicines(String sql, Object... params) {
        List<Medicine> medicines = new ArrayList<>();
        try (Connection connection = MySqlConnectionHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    medicines.add(mapResultSetToMedicine(resultSet));
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error executing database query", ex);
        }
        return medicines;
    }

    /**
     * Retrieves all available medicine records.
     *
     * @return A {@link List} of all {@link Medicine} records.
     */
    @Override
    public List<Medicine> getAllMedicine() {
        String sql = "SELECT medicine.id, medicine.medicine_name, medicine.unit, " +
                "medicine.category_id, medicine_category.category_name " +
                "FROM medicine " +
                "LEFT JOIN medicine_category ON medicine.category_id = medicine_category.id " +
                "ORDER BY medicine.medicine_name";
        return fetchMedicines(sql);
    }

    /**
     * Retrieves the medicine record that matches the given ID.
     *
     * @param id The ID of the medicine to look up.
     * @return The {@link Medicine} record, or null if it is not found.
     */
    @Override
    public Medicine getMedicineById(String id) {
        String sql = "SELECT medicine.id, medicine.medicine_name, medicine.unit, " +
                "medicine.category_id, medicine_category.category_name " +
                "FROM medicine " +
                "LEFT JOIN medicine_category ON medicine.category_id = medicine_category.id " +
                "WHERE medicine.id = ?";
        List<Medicine> result = fetchMedicines(sql, id);
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Searches for medicine records that match the given keyword.
     *
     * @param keyword The keyword to search for in medicine names or categories.
     * @return A {@link List} of matching {@link Medicine} records.
     */
    @Override
    public List<Medicine> searchMedicine(String keyword) {
        String sql = "SELECT medicine.id, medicine.medicine_name, medicine.unit, " +
                "medicine.category_id, medicine_category.category_name " +
                "FROM medicine " +
                "LEFT JOIN medicine_category ON medicine.category_id = medicine_category.id " +
                "WHERE medicine.medicine_name LIKE ? " +
                "OR medicine_category.category_name LIKE ? " +
                "ORDER BY medicine.medicine_name";
        String pattern = "%" + keyword.trim() + "%";
        return fetchMedicines(sql, pattern, pattern);
    }
}