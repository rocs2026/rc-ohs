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
     * Retrieves all medicine records from the database.
     *
     * @return a list of all recorded medicines, or an empty list if none exist.
     */
    @Override
    public List<Medicine> getAllMedicine() {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT medicine.id, medicine.medicine_name, "
                + "medicine.unit, medicine.category_id, "
                + "medicine_category.category_name "
                + "FROM medicine "
                + "LEFT JOIN medicine_category "
                + "ON medicine.category_id = medicine_category.id "
                + "ORDER BY medicine.medicine_name;";

        try (Connection connection = ConnectionHelper.mySqlGetConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Medicine medicine = new Medicine();
                medicine.setId(rs.getString("id"));
                medicine.setMedicineName(rs.getString("medicine_name"));
                medicine.setUnit(rs.getString("unit"));
                MedicineCategory category = new MedicineCategory();
                category.setId(rs.getString("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                medicine.setCategory(category);

                medicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicines;
    }

    /**
     * Retrieves a specific medicine record by its unique identifier.
     *
     * @param id the unique identifier of the medicine to look up.
     * @return the Medicine object if found, or null if it does not exist.
     */
    @Override
    public Medicine getMedicineById(String id) {
        Medicine medicine = null;
        String sql = "SELECT medicine.id, medicine.medicine_name, "
                + "medicine.unit, medicine.category_id, "
                + "medicine_category.category_name "
                + "FROM medicine "
                + "LEFT JOIN medicine_category "
                + "ON medicine.category_id = medicine_category.id "
                + "WHERE medicine.id = ?;";

        try (Connection connection = ConnectionHelper.mySqlGetConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    medicine = new Medicine();
                    medicine.setId(rs.getString("id"));
                    medicine.setMedicineName(rs.getString("medicine_name"));
                    medicine.setUnit(rs.getString("unit"));
                    MedicineCategory category = new MedicineCategory();
                    category.setId(rs.getString("category_id"));
                    category.setCategoryName(rs.getString("category_name"));
                    medicine.setCategory(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicine;
    }

    /**
     * Searches for medicine records matching the specified name or keyword.
     *
     * @param keyword the search keyword or medicine name.
     * @return a list of matching medicine records.
     */
    @Override
    public List<Medicine> searchMedicine(String keyword) {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT medicine.id, medicine.medicine_name, "
                + "medicine.unit, medicine.category_id, "
                + "medicine_category.category_name "
                + "FROM medicine "
                + "LEFT JOIN medicine_category "
                + "ON medicine.category_id = medicine_category.id "
                + "WHERE medicine.medicine_name LIKE ? "
                + "ORDER BY medicine.medicine_name;";

        try (Connection connection = ConnectionHelper.mySqlGetConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + keyword + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Medicine medicine = new Medicine();
                    medicine.setId(rs.getString("id"));
                    medicine.setMedicineName(rs.getString("medicine_name"));
                    medicine.setUnit(rs.getString("unit"));

                    MedicineCategory category = new MedicineCategory();
                    category.setId(rs.getString("category_id"));
                    category.setCategoryName(rs.getString("category_name"));
                    medicine.setCategory(category);

                    medicines.add(medicine);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicines;
    }
}
