
package com.rocs.rc.ohs.data.dao.medicinestock.impl;

import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.app.model.medicine.MedicineCategory;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicinestock.MedicineStockDao;
import com.rocs.rc.ohs.data.utils.QueryConstants;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Handles database operations for medicine stocks.
 */
public class MedicineStockDaoImpl implements MedicineStockDao {

    /**
     * Minimum remaining quantity before stock is considered low.
     */
    private static final int LOW_STOCK_LEVEL = 10;

    /**
     * Status used when the remaining quantity is low.
     */
    private static final String STATUS_LOW_STOCK = "Low Stock";

    /**
     * Status used when the remaining quantity is enough.
     */
    private static final String STATUS_ENOUGH = "Enough";

    /**
     * Gets all medicine stocks from the database.
     *
     * @return All medicine stocks, or an empty list if none are found.
     */
    @Override
    public List<MedicineStock> findAll() {
        List<MedicineStock> stocks = new ArrayList<>();

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     QueryConstants.SELECT_STOCK
                             + "order by ms.batch_number")) {

            stocks = readStocks(stmt);

        } catch (SQLException e) {
            return stocks;
        }

        return stocks;
    }

    /**
     * Finds a stock using its ID.
     *
     * @param id The stock record ID.
     * @return The matching stock, or null if not found.
     */
    @Override
    public MedicineStock findById(String id) {
        MedicineStock stock = null;

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     QueryConstants.SELECT_STOCK + "where ms.id = ?")) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stock = mapStock(rs);
                }
            }

        } catch (SQLException e) {
            return stock;
        }

        return stock;
    }

    /**
     * Gets stocks that have reached the minimum quantity.
     *
     * @return A list of low stock records.
     */
    @Override
    public List<MedicineStock> findLowStock() {
        List<MedicineStock> stocks = new ArrayList<>();

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     QueryConstants.SELECT_STOCK
                             + "where fn_remaining_stock(ms.id) <= ? "
                             + "order by ms.batch_number")) {

            stmt.setInt(1, LOW_STOCK_LEVEL);
            stocks = readStocks(stmt);

        } catch (SQLException e) {
            return stocks;
        }

        return stocks;
    }

    /**
     * Searches stocks by medicine name.
     * The search ignores case and accepts partial names.
     * Returns all stocks if the keyword is empty or null.
     *
     * @param keyword The name or keyword to search for.
     * @return The matching medicine stocks.
     */
    @Override
    public List<MedicineStock> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }

        String pattern = "%"
                + keyword.trim().toLowerCase(Locale.ROOT) + "%";

        List<MedicineStock> stocks = new ArrayList<>();

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     QueryConstants.SELECT_STOCK
                             + "where lower(m.medicine_name) like ? "
                             + "order by ms.batch_number")) {

            stmt.setString(1, pattern);
            stocks = readStocks(stmt);

        } catch (SQLException e) {
            return stocks;
        }

        return stocks;
    }

    /**
     * Updates the received quantity and stock status.
     *
     * @param id The stock record to update.
     * @param quantity The new quantity. Negative values are not allowed.
     * @return true if the record was updated.
     * @throws IllegalArgumentException if the quantity is negative or lower
     *         than the quantity already dispensed.
     */
    @Override
    public boolean updateQuantity(String id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity must not be negative.");
        }

        try (Connection conn = ConnectionHelper.getConnection()) {
            int dispensed = getDispensedQuantity(conn, id);

            if (quantity < dispensed) {
                throw new IllegalArgumentException(
                        "Quantity must not be lower than the quantity "
                                + "already dispensed.");
            }

            String status = (quantity - dispensed) <= LOW_STOCK_LEVEL
                    ? STATUS_LOW_STOCK : STATUS_ENOUGH;

            try (PreparedStatement stmt = conn.prepareStatement(
                    QueryConstants.UPDATE_STOCK_QUANTITY)) {

                stmt.setInt(1, quantity);
                stmt.setString(2, status);
                stmt.setString(3, id);

                return stmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            return false;
        }
    }

    // Executes the query and maps each result row to a stock object.
    private List<MedicineStock> readStocks(PreparedStatement stmt)
            throws SQLException {
        List<MedicineStock> stocks = new ArrayList<>();

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stocks.add(mapStock(rs));
            }
        }

        return stocks;
    }

    // Gets the quantity already dispensed for the selected stock.
    private int getDispensedQuantity(Connection conn, String id)
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                QueryConstants.SELECT_DISPENSED_QUANTITY)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    // Maps the current result row to a MedicineStock object.
    private MedicineStock mapStock(ResultSet rs) throws SQLException {
        MedicineStock stock = new MedicineStock();

        stock.setId(rs.getString("id"));
        stock.setMedicine(mapMedicine(rs));
        stock.setBatchNumber(rs.getString("batch_number"));
        stock.setQuantityReceived(rs.getInt("quantity_received"));

        Date expiration = rs.getDate("expiration_date");

        if (expiration != null) {
            stock.setExpirationDate(
                    expiration.toLocalDate().atStartOfDay());
        } else {
            stock.setExpirationDate(null);
        }

        stock.setStatus(rs.getString("status"));

        return stock;
    }

    // Gets the medicine details and category from the current result row.
    private Medicine mapMedicine(ResultSet rs) throws SQLException {
        Medicine medicine = new Medicine();

        medicine.setId(rs.getString("medicine_id"));
        medicine.setMedicineName(rs.getString("medicine_name"));
        medicine.setUnit(rs.getString("unit"));

        String categoryId = rs.getString("category_id");

        if (categoryId != null) {
            MedicineCategory category = new MedicineCategory();

            category.setId(categoryId);
            category.setCategoryName(rs.getString("category_name"));

            medicine.setCategory(category);
        }

        return medicine;
    }
}
