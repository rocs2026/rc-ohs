
package com.rocs.rc.ohs.data.dao.medicinestock.impl;

import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the database operations for medicine stocks.
 */
class MedicineStockDaoImplTest {

    /** Mocked connection helper. */
    private MockedStatic<ConnectionHelper> connectionHelper;

    /** Mock database connection. */
    private Connection connection;

    /** Statement used for retrieving medicine stocks. */
    private PreparedStatement statement;

    /** Result returned by the stock queries. */
    private ResultSet resultSet;

    /** Statement used to retrieve dispensed quantities. */
    private PreparedStatement dispensedStatement;

    /** Result containing the dispensed quantity. */
    private ResultSet dispensedResultSet;

    /** Statement used to update stock quantities. */
    private PreparedStatement updateStatement;

    /** DAO being tested. */
    private MedicineStockDaoImpl dao;

    @BeforeEach
    void setUp() throws SQLException {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        dispensedStatement = mock(PreparedStatement.class);
        dispensedResultSet = mock(ResultSet.class);
        updateStatement = mock(PreparedStatement.class);

        connectionHelper = mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        when(connection.prepareStatement(startsWith("SELECT COALESCE")))
                .thenReturn(dispensedStatement);

        when(connection.prepareStatement(startsWith("UPDATE")))
                .thenReturn(updateStatement);

        when(statement.executeQuery()).thenReturn(resultSet);
        when(dispensedStatement.executeQuery()).thenReturn(dispensedResultSet);
        when(updateStatement.executeUpdate()).thenReturn(1);

        dao = new MedicineStockDaoImpl();
    }

    @AfterEach
    void tearDown() {
        connectionHelper.close();
    }

    @Test
    void findAllReturnsMappedStocksIncludingNullExpirationDate()
            throws SQLException {

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("id")).thenReturn("1", "2");
        when(resultSet.getString("batch_number"))
                .thenReturn("B-001", "B-002");
        when(resultSet.getInt("quantity_received")).thenReturn(50, 20);
        when(resultSet.getDate("expiration_date"))
                .thenReturn(Date.valueOf(LocalDate.of(2027, 1, 31)), (Date) null);
        when(resultSet.getString("status"))
                .thenReturn("Enough", "Low Stock");

        List<MedicineStock> stocks = dao.findAll();

        assertEquals(2, stocks.size());

        assertEquals("1", stocks.get(0).getId());
        assertEquals("B-001", stocks.get(0).getBatchNumber());
        assertEquals(50, stocks.get(0).getQuantityReceived());
        assertEquals(
                LocalDate.of(2027, 1, 31).atStartOfDay(),
                stocks.get(0).getExpirationDate()
        );
        assertEquals("Enough", stocks.get(0).getStatus());

        assertEquals("2", stocks.get(1).getId());
        assertNull(stocks.get(1).getExpirationDate());
        assertEquals("Low Stock", stocks.get(1).getStatus());

        verify(connection).prepareStatement(
                "SELECT * FROM medicine_stock ORDER BY batch_number"
        );
    }

    @Test
    void findAllReturnsEmptyListWhenNoStocksExistOrDatabaseFails()
            throws SQLException {

        when(resultSet.next()).thenReturn(false);

        List<MedicineStock> noRows = dao.findAll();

        assertNotNull(noRows);
        assertTrue(noRows.isEmpty());

        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("connection failed"));

        List<MedicineStock> onFailure = dao.findAll();

        assertNotNull(onFailure);
        assertTrue(onFailure.isEmpty());
    }

    @Test
    void findByIdReturnsMappedStockForTheGivenId() throws SQLException {

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("id")).thenReturn("1");
        when(resultSet.getString("batch_number")).thenReturn("B-001");
        when(resultSet.getInt("quantity_received")).thenReturn(50);
        when(resultSet.getDate("expiration_date"))
                .thenReturn(Date.valueOf(LocalDate.of(2027, 1, 31)));
        when(resultSet.getString("status")).thenReturn("Enough");

        MedicineStock stock = dao.findById("1");

        assertNotNull(stock);
        assertEquals("1", stock.getId());
        assertEquals("B-001", stock.getBatchNumber());
        assertEquals(50, stock.getQuantityReceived());
        assertEquals(
                LocalDate.of(2027, 1, 31).atStartOfDay(),
                stock.getExpirationDate()
        );
        assertEquals("Enough", stock.getStatus());

        verify(statement).setString(1, "1");
    }

    @Test
    void findByIdReturnsNullWhenStockIsNotFoundOrDatabaseFails()
            throws SQLException {

        when(resultSet.next()).thenReturn(false);

        assertNull(dao.findById("missing"));
        verify(statement).setString(1, "missing");

        when(statement.executeQuery())
                .thenThrow(new SQLException("query failed"));

        assertNull(dao.findById("1"));
    }

    @Test
    void findLowStockReturnsStocksUsingTheLowStockLevel()
            throws SQLException {

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("1");
        when(resultSet.getString("status")).thenReturn("Low Stock");

        List<MedicineStock> stocks = dao.findLowStock();

        assertEquals(1, stocks.size());
        assertEquals("1", stocks.get(0).getId());
        assertEquals("Low Stock", stocks.get(0).getStatus());

        verify(statement).setInt(1, 10);
    }

    @Test
    void findLowStockReturnsEmptyListWhenNoStockIsLowOrDatabaseFails()
            throws SQLException {

        when(resultSet.next()).thenReturn(false);

        List<MedicineStock> noneLow = dao.findLowStock();

        assertNotNull(noneLow);
        assertTrue(noneLow.isEmpty());

        when(statement.executeQuery())
                .thenThrow(new SQLException("query failed"));

        List<MedicineStock> onFailure = dao.findLowStock();

        assertNotNull(onFailure);
        assertTrue(onFailure.isEmpty());
    }

    @Test
    void updateQuantityRejectsNegativeQuantityWithoutTouchingTheDatabase() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.updateQuantity("1", -1)
        );

        connectionHelper.verify(ConnectionHelper::getConnection, never());
    }

    @Test
    void updateQuantityRejectsQuantityLowerThanDispensedButAcceptsEqualQuantity()
            throws SQLException {

        stubDispensed(20);

        assertThrows(
                IllegalArgumentException.class,
                () -> dao.updateQuantity("1", 19)
        );

        verify(connection, never()).prepareStatement(startsWith("UPDATE"));

        assertTrue(dao.updateQuantity("1", 20));
        verify(updateStatement).setInt(1, 20);
    }

    @Test
    void updateQuantitySavesQuantityAndDerivesStatusFromRemainingStock()
            throws SQLException {

        stubDispensed(5);

        assertTrue(dao.updateQuantity("1", 15));

        verify(dispensedStatement, atLeastOnce())
                .setString(1, "1");
        verify(updateStatement).setInt(1, 15);
        verify(updateStatement).setString(2, "Low Stock");
        verify(updateStatement, atLeastOnce())
                .setString(3, "1");

        assertTrue(dao.updateQuantity("1", 16));

        verify(updateStatement).setInt(1, 16);
        verify(updateStatement).setString(2, "Enough");
    }

    @Test
    void updateQuantityTreatsMissingDispensedRowAsNothingDispensed()
            throws SQLException {

        when(dispensedResultSet.next()).thenReturn(false);

        assertTrue(dao.updateQuantity("1", 50));

        verify(updateStatement).setInt(1, 50);
        verify(updateStatement).setString(2, "Enough");
    }

    @Test
    void updateQuantityReturnsFalseWhenNoRowIsUpdatedOrDatabaseFails()
            throws SQLException {

        stubDispensed(0);
        when(updateStatement.executeUpdate()).thenReturn(0);

        assertFalse(dao.updateQuantity("missing", 50));

        when(dispensedStatement.executeQuery())
                .thenThrow(new SQLException("query failed"));

        assertFalse(dao.updateQuantity("1", 50));
    }

    /**
     * Sets the quantity returned by the dispensed stock query.
     *
     * @param dispensed the quantity already dispensed.
     * @throws SQLException if the result cannot be configured.
     */
    private void stubDispensed(int dispensed) throws SQLException {
        when(dispensedResultSet.next()).thenReturn(true);
        when(dispensedResultSet.getInt(1)).thenReturn(dispensed);
    }
}
