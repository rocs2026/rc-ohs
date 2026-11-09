package com.rocs.rc.ohs.data.dao.medicinestock.impl;

import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.medicinestock.MedicineStockDao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the data access operations for the medicine stock inventory.
 */
public class MedicineStockDaoImpl implements MedicineStockDao {

    /** The remaining quantity at or below which a stock is low stock. */
    private static final int LOW_STOCK_LEVEL = 10;

    /** The status of a stock that reached the minimum stock level. */
    private static final String STATUS_LOW_STOCK = "Low Stock";

    /** The status of a stock that is above the minimum stock level. */
    private static final String STATUS_ENOUGH = "Enough";

    /**
     * Retrieves all medicine stocks.
     *
     * @return The list of {@link MedicineStock}, or an empty list if none.
     */
    @Override
    public List<MedicineStock> findAll() {
        List<MedicineStock> stocks = new ArrayList<>();
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "select * from medicine_stock order by batch_number");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stocks.add(mapStock(rs));
            }
        } catch (SQLException e) {
            System.out.println(
                    "An exception was thrown while finding medicine stocks. "
                            + e.getMessage());
        }
        return stocks;
    }

    /**
     * Retrieves the medicine stock with the given id.
     *
     * @param id The id of the medicine stock.
     * @return The {@link MedicineStock}, or null if it is not found.
     */
    @Override
    public MedicineStock findById(String id) {
        MedicineStock stock = null;
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "select * from medicine_stock where id = ?")) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stock = mapStock(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println(
                    "An exception was thrown while finding a medicine stock. "
                            + e.getMessage());
        }
        return stock;
    }

    /**
     * Retrieves the medicine stocks that reached the minimum stock level.
     *
     * @return The list of low stock {@link MedicineStock}.
     */
    @Override
    public List<MedicineStock> findLowStock() {
        List<MedicineStock> stocks = new ArrayList<>();
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "select * from medicine_stock "
                             + "where fn_remaining_stock(id) <= ? "
                             + "order by batch_number")) {
            stmt.setInt(1, LOW_STOCK_LEVEL);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stocks.add(mapStock(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(
                    "An exception was thrown while finding low stock items. "
                            + e.getMessage());
        }
        return stocks;
    }

    /**
     * Updates the received quantity of a medicine stock and its status.
     *
     * @param id The id of the medicine stock.
     * @param quantity The new quantity, which must not be negative.
     * @return true if the quantity was saved.
     * @throws IllegalArgumentException if the quantity is negative or lower
     *         than the quantity already dispensed.
     */
    @Override
    public boolean updateQuantity(String id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must not be negative.");
        }
        try (Connection conn = ConnectionHelper.getConnection()) {
            int dispensed = getDispensedQuantity(conn, id);
            if (quantity < dispensed) {
                throw new IllegalArgumentException(
                        "Quantity must not be lower than the quantity already dispensed.");
            }
            String status = (quantity - dispensed) <= LOW_STOCK_LEVEL
                    ? STATUS_LOW_STOCK : STATUS_ENOUGH;
            try (PreparedStatement stmt = conn.prepareStatement(
                    "update medicine_stock set quantity_received = ?, status = ? "
                            + "where id = ?")) {
                stmt.setInt(1, quantity);
                stmt.setString(2, status);
                stmt.setString(3, id);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println(
                    "An exception was thrown while updating the quantity. "
                            + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieves the total quantity already dispensed from a medicine stock.
     *
     * @param conn The open database connection.
     * @param id The id of the medicine stock.
     * @return The total dispensed quantity, or 0 if nothing was dispensed.
     * @throws SQLException if the query fails.
     */
    private int getDispensedQuantity(Connection conn, String id)
            throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "select coalesce(sum(quantity_to_dispense), 0) "
                        + "from prescription where medicine_stock_id = ?")) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /**
     * Maps the current row of the result set to a {@link MedicineStock}.
     *
     * @param rs The result set positioned at the row to map.
     * @return The mapped {@link MedicineStock}.
     * @throws SQLException if a column cannot be read.
     */
    private MedicineStock mapStock(ResultSet rs) throws SQLException {
        MedicineStock stock = new MedicineStock();
        stock.setId(rs.getString("id"));
        stock.setBatchNumber(rs.getString("batch_number"));
        stock.setQuantityReceived(rs.getInt("quantity_received"));
        Date expiration = rs.getDate("expiration_date");
        stock.setExpirationDate(
                expiration == null ? null : expiration.toLocalDate().atStartOfDay());
        stock.setStatus(rs.getString("status"));
        return stock;
    }
}
