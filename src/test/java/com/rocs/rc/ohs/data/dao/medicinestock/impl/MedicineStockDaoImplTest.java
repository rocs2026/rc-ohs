
package com.rocs.rc.ohs.data.dao.medicinestock.impl;

import com.rocs.rc.ohs.app.model.medicine.Medicine;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.stubbing.OngoingStubbing;

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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the database operations for medicine stocks.
 */
class MedicineStockDaoImplTest {

    /** Expected status when the remaining stock is enough. */
    private static final String STATUS_ENOUGH = "Enough";

    /** Mock for the static database connection method. */
    private MockedStatic<ConnectionHelper> connectionHelper;

    /** Mock database connection used by the DAO. */
    private Connection connection;

    /** Prepared statement for medicine stock queries. */
    private PreparedStatement statement;

    /** Mocked medicine stock query results. */
    private ResultSet resultSet;

    /** Prepared statement for dispensed quantity queries. */
    private PreparedStatement dispensedStatement;

    /** Result of the dispensed quantity query. */
    private ResultSet dispensedResultSet;

    /** Prepared statement for stock updates. */
    private PreparedStatement updateStatement;

    /** DAO being tested in this class. */
    private MedicineStockDaoImpl dao;

    /**
     * Initializes the mocked database objects before each test.
     *
     * @throws SQLException if mock database setup fails.
     */
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
        when(connection.prepareStatement(startsWith("select coalesce")))
                .thenReturn(dispensedStatement);
        when(connection.prepareStatement(startsWith("update")))
                .thenReturn(updateStatement);

        when(statement.executeQuery()).thenReturn(resultSet);
        when(dispensedStatement.executeQuery()).thenReturn(dispensedResultSet);
        when(updateStatement.executeUpdate()).thenReturn(1);

        dao = new MedicineStockDaoImpl();
    }

    /**
     * Closes the static database mock after each test.
     */
    @AfterEach
    void tearDown() {
        connectionHelper.close();
    }

    @Test
    @DisplayName("RCISD-5-TC1, TC2, TC4: all medicine stocks with details")
    void findAllReturnsEveryMedicineStockWithItsMedicineDetails()
            throws SQLException {

        stubRows(0, 1);

        List<MedicineStock> stocks = dao.findAll();

        assertEquals(2, stocks.size());
        assertStock(stocks.get(0), 0);
        assertStock(stocks.get(1), 1);
    }

    @Test
    @DisplayName("RCISD-5-TC7: no medicine records")
    void findAllReturnsEmptyListWhenNoMedicineRecordsExistOrDatabaseFails()
            throws SQLException {

        stubRows();

        assertTrue(dao.findAll().isEmpty());

        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("connection failed"));

        List<MedicineStock> onFailure = dao.findAll();

        assertNotNull(onFailure);
        assertTrue(onFailure.isEmpty());
    }

    @Test
    @DisplayName("RCISD-5-TC3: details of the selected medicine stock")
    void findByIdReturnsStockWithMedicineDetailsForTheGivenId()
            throws SQLException {

        String stockId = "cff69d39-caa4-4a79-978b-8927f5b0c1a4";

        stubRows(0);

        MedicineStock stock = dao.findById(stockId);

        assertNotNull(stock);
        assertStock(stock, 0);

        verify(statement).setString(1, stockId);
    }

    @Test
    void findByIdLeavesExpirationDateAndCategoryEmptyWhenNotRecorded()
            throws SQLException {

        String stockId = "cff69d39-caa4-4a79-978b-8927f5b0c1a4";

        stubRows(0);
        when(resultSet.getDate("expiration_date")).thenReturn((Date) null);
        when(resultSet.getString("category_id")).thenReturn((String) null);

        MedicineStock stock = dao.findById(stockId);

        assertNotNull(stock);
        assertNull(stock.getExpirationDate());
        assertNotNull(stock.getMedicine());
        assertNull(stock.getMedicine().getCategory());
    }

    @Test
    void findByIdReturnsNullWhenStockIsNotFoundOrDatabaseFails()
            throws SQLException {

        String stockId = "cff69d39-caa4-4a79-978b-8927f5b0c1a4";

        stubRows();

        assertNull(dao.findById("missing"));

        verify(statement).setString(1, "missing");

        when(statement.executeQuery()).thenThrow(new SQLException("failed"));

        assertNull(dao.findById(stockId));
    }

    @Test
    @DisplayName("RCISD-5-TC5: search using the full medicine name")
    void searchReturnsTheMatchingStockWhenTheFullMedicineNameIsGiven()
            throws SQLException {

        stubRows(1);

        List<MedicineStock> stocks = dao.search("Amoxicillin 500mg");

        assertEquals(1, stocks.size());
        assertStock(stocks.get(0), 1);

        verify(statement).setString(1, "%amoxicillin 500mg%");
    }

    @Test
    @DisplayName("RCISD-5-TC6: search using a keyword")
    void searchReturnsTheMatchingStockWhenAKeywordIsGivenIgnoringCase()
            throws SQLException {

        stubRows(0);

        List<MedicineStock> stocks = dao.search("Paracetamol");

        assertEquals(1, stocks.size());
        assertStock(stocks.get(0), 0);

        stubRows(0);

        assertEquals(1, dao.search("  PARACETAMOL ").size());

        verify(statement, times(2)).setString(1, "%paracetamol%");
    }

    @Test
    void searchReturnsAllStocksWhenTheKeywordIsBlank() throws SQLException {

        stubRows(0, 1);

        assertEquals(2, dao.search("   ").size());

        stubRows(0, 1);

        assertEquals(2, dao.search(null).size());

        verify(statement, never()).setString(anyInt(), anyString());
    }

    @Test
    void searchReturnsEmptyListWhenNothingMatchesOrDatabaseFails()
            throws SQLException {

        stubRows();

        assertTrue(dao.search("Ibuprofen").isEmpty());

        when(statement.executeQuery()).thenThrow(new SQLException("failed"));

        List<MedicineStock> onFailure = dao.search("Ibuprofen");

        assertNotNull(onFailure);
        assertTrue(onFailure.isEmpty());
    }

    @Test
    void findLowStockReturnsStocksUsingTheLowStockLevel()
            throws SQLException {

        stubRows(1);

        List<MedicineStock> stocks = dao.findLowStock();

        assertEquals(1, stocks.size());
        assertStock(stocks.get(0), 1);
        verify(statement).setInt(1, 10);
    }

    @Test
    void findLowStockReturnsEmptyListWhenNoStockIsLowOrDatabaseFails()
            throws SQLException {

        stubRows();

        assertTrue(dao.findLowStock().isEmpty());

        when(statement.executeQuery()).thenThrow(new SQLException("failed"));

        List<MedicineStock> onFailure = dao.findLowStock();

        assertNotNull(onFailure);
        assertTrue(onFailure.isEmpty());
    }

    @Test
    void updateQuantityRejectsInvalidQuantitiesButAcceptsEqualToDispensed()
            throws SQLException {

        String stockId = "cff69d39-caa4-4a79-978b-8927f5b0c1a4";

        assertThrows(IllegalArgumentException.class,
                () -> dao.updateQuantity(stockId, -1));

        connectionHelper.verify(ConnectionHelper::getConnection, never());

        stubDispensed(20);

        assertThrows(IllegalArgumentException.class,
                () -> dao.updateQuantity(stockId, 19));

        verify(connection, never()).prepareStatement(startsWith("update"));

        assertTrue(dao.updateQuantity(stockId, 20));
        verify(updateStatement).setInt(1, 20);
    }

    @Test
    void updateQuantitySavesQuantityAndDerivesStatusFromRemainingStock()
            throws SQLException {

        String stockId = "cff69d39-caa4-4a79-978b-8927f5b0c1a4";

        stubDispensed(5);

        assertTrue(dao.updateQuantity(stockId, 15));
        verify(updateStatement).setInt(1, 15);
        verify(updateStatement).setString(2, "Low Stock");

        assertTrue(dao.updateQuantity(stockId, 16));
        verify(updateStatement).setInt(1, 16);
        verify(updateStatement).setString(2, STATUS_ENOUGH);

        verify(updateStatement, times(2)).setString(3, stockId);
    }

    @Test
    void updateQuantityTreatsMissingDispensedRowAsNothingDispensed()
            throws SQLException {

        when(dispensedResultSet.next()).thenReturn(false);

        String stockId = "cff69d39-caa4-4a79-978b-8927f5b0c1a4";

        assertTrue(dao.updateQuantity(stockId, 50));

        verify(updateStatement).setInt(1, 50);
        verify(updateStatement).setString(2, STATUS_ENOUGH);
    }

    @Test
    void updateQuantityReturnsFalseWhenNoRowIsUpdatedOrDatabaseFails()
            throws SQLException {

        String stockId = "cff69d39-caa4-4a79-978b-8927f5b0c1a4";

        stubDispensed(0);

        when(updateStatement.executeUpdate()).thenReturn(0);

        assertFalse(dao.updateQuantity("missing", 50));

        when(dispensedStatement.executeQuery())
                .thenThrow(new SQLException("failed"));

        assertFalse(dao.updateQuantity(stockId, 50));
    }

    /**
     * Checks stock details against the expected sample data.
     *
     * @param stock the stock record returned by the DAO.
     * @param row the index of the expected sample data.
     */
    private void assertStock(MedicineStock stock, int row) {

        String[] stockIds = {
                "cff69d39-caa4-4a79-978b-8927f5b0c1a4",
                "70aaf305-a7f5-41f1-bd0a-6ad73c042be9"
        };

        String[] medicineIds = {
                "ebb3209c-50ea-4fbd-8b7e-be4540ab5b57",
                "36c00788-fcfd-485e-8ad3-de08fd49cb76"
        };

        String[] medicineNames = {
                "Paracetamol 500mg", "Amoxicillin 500mg"
        };

        String[] units = {"tablet", "capsule"};

        String[] categoryIds = {
                "fde2280e-3564-4ced-87d5-a06454eec0b3",
                "e855961e-acf2-4bec-9262-30d1961d40b1"
        };

        String[] categoryNames = {"Analgesic", "Antibiotic"};
        String[] batchNumbers = {"B-001", "B-002"};
        String[] expirationDates = {"2027-12-31", "2027-06-30"};

        int quantityReceived = 50;

        assertEquals(stockIds[row], stock.getId());
        assertEquals(batchNumbers[row], stock.getBatchNumber());
        assertEquals(quantityReceived, stock.getQuantityReceived());
        assertEquals(LocalDate.parse(expirationDates[row]).atStartOfDay(),
                stock.getExpirationDate());
        assertEquals(STATUS_ENOUGH, stock.getStatus());

        Medicine medicine = stock.getMedicine();

        assertNotNull(medicine);
        assertEquals(medicineIds[row], medicine.getId());
        assertEquals(medicineNames[row], medicine.getMedicineName());
        assertEquals(units[row], medicine.getUnit());

        assertNotNull(medicine.getCategory());
        assertEquals(categoryIds[row], medicine.getCategory().getId());
        assertEquals(categoryNames[row],
                medicine.getCategory().getCategoryName());
    }

    /**
     * Prepares the mocked result set using the selected sample records.
     *
     * @param rows the sample data indexes to include. Empty means no records.
     * @throws SQLException if setting up the mocked result set fails.
     */
    private void stubRows(int... rows) throws SQLException {

        String[] stockIds = {
                "cff69d39-caa4-4a79-978b-8927f5b0c1a4",
                "70aaf305-a7f5-41f1-bd0a-6ad73c042be9"
        };

        String[] medicineIds = {
                "ebb3209c-50ea-4fbd-8b7e-be4540ab5b57",
                "36c00788-fcfd-485e-8ad3-de08fd49cb76"
        };

        String[] medicineNames = {
                "Paracetamol 500mg", "Amoxicillin 500mg"
        };

        String[] units = {"tablet", "capsule"};

        String[] categoryIds = {
                "fde2280e-3564-4ced-87d5-a06454eec0b3",
                "e855961e-acf2-4bec-9262-30d1961d40b1"
        };

        String[] categoryNames = {"Analgesic", "Antibiotic"};
        String[] batchNumbers = {"B-001", "B-002"};
        String[] expirationDates = {"2027-12-31", "2027-06-30"};

        OngoingStubbing<Boolean> hasNext = when(resultSet.next());

        for (int i = 0; i < rows.length; i++) {
            hasNext = hasNext.thenReturn(true);
        }

        hasNext.thenReturn(false);

        stubColumn("id", stockIds, rows);
        stubColumn("batch_number", batchNumbers, rows);
        stubColumn("medicine_id", medicineIds, rows);
        stubColumn("medicine_name", medicineNames, rows);
        stubColumn("unit", units, rows);
        stubColumn("category_id", categoryIds, rows);
        stubColumn("category_name", categoryNames, rows);

        if (rows.length > 0) {
            OngoingStubbing<Date> expiration =
                    when(resultSet.getDate("expiration_date"));

            for (int row : rows) {
                expiration = expiration.thenReturn(
                        Date.valueOf(expirationDates[row]));
            }
        }

        when(resultSet.getInt("quantity_received")).thenReturn(50);
        when(resultSet.getString("status")).thenReturn(STATUS_ENOUGH);
    }

    /**
     * Sets up a mocked result column using the selected sample rows.
     *
     * @param column the database column name.
     * @param values the sample values for that column.
     * @param rows the indexes of the records to include.
     * @throws SQLException if setting up the mocked column fails.
     */
    private void stubColumn(String column, String[] values, int... rows)
            throws SQLException {

        if (rows.length == 0) {
            return;
        }

        OngoingStubbing<String> stub = when(resultSet.getString(column));

        for (int row : rows) {
            stub = stub.thenReturn(values[row]);
        }
    }

    /**
     * Sets the mocked result for the quantity already dispensed.
     *
     * @param dispensed the number of units already dispensed.
     * @throws SQLException if setting up the mocked result fails.
     */
    private void stubDispensed(int dispensed) throws SQLException {

        when(dispensedResultSet.next()).thenReturn(true);
        when(dispensedResultSet.getInt(1)).thenReturn(dispensed);
    }
}
