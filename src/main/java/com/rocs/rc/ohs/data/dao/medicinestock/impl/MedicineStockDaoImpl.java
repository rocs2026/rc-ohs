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

/*
 * Handles database access for medicine stock records.
 */
public class MedicineStockDaoImpl implements MedicineStockDao {

    // Inventory threshold used for low stock alerts.
    private static final int LOW_STOCK_LEVEL = 10;

    private static final String STATUS_LOW_STOCK = "Low Stock";
    private static final String STATUS_ENOUGH = "Enough";

    /*
     * Loads all medicine stock records ordered by batch number.
     */
    @Override
    public List<MedicineStock> findAll() {
        List<MedicineStock> stocks = new ArrayList<>();

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM medicine_stock ORDER BY batch_number");
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

    /*
     * Retrieves a stock record using its ID.
     */
    @Override
    public MedicineStock findById(String id) {
        MedicineStock stock = null;

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM medicine_stock WHERE id = ?")) {

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

    /*
     * Returns stocks that have reached the configured threshold.
     */
    @Override
    public List<MedicineStock> findLowStock() {
        List<MedicineStock> stocks = new ArrayList<>();

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM medicine_stock "
                             + "WHERE fn_remaining_stock(id) <= ? "
                             + "ORDER BY batch_number")) {

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

    /*
     * Updates quantity and recalculates the stock status.
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
                    ? STATUS_LOW_STOCK
                    : STATUS_ENOUGH;

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE medicine_stock "
                            + "SET quantity_received = ?, status = ? "
                            + "WHERE id = ?")) {

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

    /*
     * Retrieves the total dispensed quantity for a stock item.
     */
    private int getDispensedQuantity(Connection conn, String id)
            throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT COALESCE(sum(quantity_to_dispense), 0) "
                        + "FROM prescription "
                        + "WHERE medicine_stock_id = ?")) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    /*
     * Maps the current row into a MedicineStock object.
     */
    private MedicineStock mapStock(ResultSet rs) throws SQLException {
        MedicineStock stock = new MedicineStock();

        stock.setId(rs.getString("id"));
        stock.setBatchNumber(rs.getString("batch_number"));
        stock.setQuantityReceived(rs.getInt("quantity_received"));

        Date expiration = rs.getDate("expiration_date");

        stock.setExpirationDate(
                expiration == null
                        ? null
                        : expiration.toLocalDate().atStartOfDay()
        );

        stock.setStatus(rs.getString("status"));

        return stock;
    }
}