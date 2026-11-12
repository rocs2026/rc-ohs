
package com.rocs.rc.ohs.data.dao.medicine.impl;

import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.app.model.medicine.MedicineCategory;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicine.MedicineStockDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the MedicineStockDao interface.
 */
public class MedicineStockDaoImpl implements MedicineStockDao {

    /**
     * This is the connection for all the operations.
     */
    private Connection connection;

    /**
     * Establishes the database connection.
     */
    public MedicineStockDaoImpl() {
        try {
            connection = ConnectionHelper.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to establish connection", e);
        }
    }

    /**
     * Retrieves all medicine stock records.
     *
     * @return a list of all medicine stock records.
     */
    @Override
    public List<MedicineStock> getAllStock() {
        List<MedicineStock> stocks = new ArrayList<>();

        String sql = "SELECT medicine.id, medicine.medicine_name, "
                + "medicine.unit, medicine_category.category_name, "
                + "medicine_stock.batch_number, "
                + "medicine_stock.quantity_received, "
                + "medicine_stock.expiration_date, medicine_stock.status "
                + "FROM medicine_stock "
                + "JOIN medicine "
                + "ON medicine_stock.medicine_id = medicine.id "
                + "LEFT JOIN medicine_category "
                + "ON medicine.category_id = medicine_category.id "
                + "ORDER BY medicine.medicine_name";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                MedicineCategory category = new MedicineCategory();
                category.setCategoryName(
                        resultSet.getString("category_name"));

                Medicine medicine = new Medicine();
                medicine.setId(resultSet.getString("id"));
                medicine.setMedicineName(
                        resultSet.getString("medicine_name"));
                medicine.setUnit(resultSet.getString("unit"));
                medicine.setCategory(category);

                MedicineStock stock = new MedicineStock();
                stock.setMedicine(medicine);
                stock.setBatchNumber(
                        resultSet.getString("batch_number"));
                stock.setQuantityReceived(
                        resultSet.getInt("quantity_received"));
                stock.setStatus(resultSet.getString("status"));

                Timestamp expiration =
                        resultSet.getTimestamp("expiration_date");

                if (expiration != null) {
                    stock.setExpirationDate(
                            expiration.toLocalDateTime());
                }

                stocks.add(stock);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Error retrieving all stock records", ex);
        }

        return stocks;
    }

    /**
     * Searches medicine stock records by medicine name or category.
     *
     * @param keyword the medicine name or category keyword.
     * @return a list of matching medicine stock records.
     */
    @Override
    public List<MedicineStock> searchStock(String keyword) {
        List<MedicineStock> stocks = new ArrayList<>();

        String sql = "SELECT medicine.id, medicine.medicine_name, "
                + "medicine.unit, medicine_category.category_name, "
                + "medicine_stock.batch_number, "
                + "medicine_stock.quantity_received, "
                + "medicine_stock.expiration_date, medicine_stock.status "
                + "FROM medicine_stock "
                + "JOIN medicine "
                + "ON medicine_stock.medicine_id = medicine.id "
                + "LEFT JOIN medicine_category "
                + "ON medicine.category_id = medicine_category.id "
                + "WHERE medicine.medicine_name LIKE ? "
                + "OR medicine_category.category_name LIKE ? "
                + "ORDER BY medicine.medicine_name";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    MedicineCategory category =
                            new MedicineCategory();
                    category.setCategoryName(
                            resultSet.getString("category_name"));

                    Medicine medicine = new Medicine();
                    medicine.setId(resultSet.getString("id"));
                    medicine.setMedicineName(
                            resultSet.getString("medicine_name"));
                    medicine.setUnit(resultSet.getString("unit"));
                    medicine.setCategory(category);

                    MedicineStock stock = new MedicineStock();
                    stock.setMedicine(medicine);
                    stock.setBatchNumber(
                            resultSet.getString("batch_number"));
                    stock.setQuantityReceived(
                            resultSet.getInt("quantity_received"));
                    stock.setStatus(resultSet.getString("status"));

                    Timestamp expiration =
                            resultSet.getTimestamp("expiration_date");

                    if (expiration != null) {
                        stock.setExpirationDate(
                                expiration.toLocalDateTime());
                    }

                    stocks.add(stock);
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Error searching stock records", ex);
        }

        return stocks;
    }
}
