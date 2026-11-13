package com.rocs.rc.ohs.data.dao.medicine.impl;

import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.app.model.medicine.MedicineCategory;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicine.MedicineStockDao;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * it is the implementation of medicine stock data access.
 */
public class MedicineStockDaoImpl implements MedicineStockDao {

    /**
     * it is the method to get all medicine stocks.
     *
     * @return a list of medicine stocks
     */
    @Override
    public List<MedicineStock> getAllStock() {
        List<MedicineStock> stocks = new ArrayList<>();

        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     QueryConstants.SELECT_ALL_MEDICINE_STOCK);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                stocks.add(mapToStock(resultSet));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Error retrieving all stock records", ex);
        }

        return stocks;
    }

    /**
     * it is the method to search medicine stock.
     *
     * @param keyword the keyword
     * @return a list of matching stocks
     */
    @Override
    public List<MedicineStock> searchStock(String keyword) {
        List<MedicineStock> stocks = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            return stocks;
        }

        String pattern = "%" + keyword.trim() + "%";

        try (Connection con = ConnectionHelper.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     QueryConstants.SEARCH_MEDICINE_STOCK)) {

            statement.setString(1, pattern);
            statement.setString(2, pattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    stocks.add(mapToStock(resultSet));
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Error searching stock records", ex);
        }

        return stocks;
    }

    /**
     * it is the helper method to map result set to stock.
     *
     * @param resultSet the result set
     * @return the mapped medicine stock
     * @throws SQLException if sql error occurs
     */
    private MedicineStock mapToStock(ResultSet resultSet)
            throws SQLException {
        MedicineCategory category = new MedicineCategory();
        category.setCategoryName(
                resultSet.getString("category_name"));

        Medicine medicine = new Medicine();
        medicine.setId(resultSet.getString("medicine_id"));
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

        return stock;
    }
}
