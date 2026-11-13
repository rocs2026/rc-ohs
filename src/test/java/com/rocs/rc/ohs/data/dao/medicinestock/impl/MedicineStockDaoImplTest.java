
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

    /** Index used to get Paracetamol test data. */
    private static final int PARACETAMOL = 0;

    /** Index used to get Amoxicillin test data. */
    private static final int AMOXICILLIN = 1;

    /** Sample IDs for the medicine stock records. */
    private static final String[] STOCK_IDS = {
            "cff69d39-caa4-4a79-978b-8927f5b0c1a4",
            "70aaf305-a7f5-41f1-bd0a-6ad73c042be9"
    };

    /** IDs of the medicines linked to each stock. */
    private static final String[] MEDICINE_IDS = {
            "ebb3209c-50ea-4fbd-8b7e-be4540ab5b57",
            "36c00788-fcfd-485e-8ad3-de08fd49cb76"
    };

    /** Medicine names used when checking the returned records. */
    private static final String[] MEDICINE_NAMES = {
            "Paracetamol 500mg", "Amoxicillin 500mg"
    };

    /** Units assigned to the sample medicines. */
    private static final String[] UNITS = {"tablet", "capsule"};

    /** Category IDs for the two sample medicines. */
    private static final String[] CATEGORY_IDS = {
            "fde2280e-3564-4ced-87d5-a06454eec0b3",
            "e855961e-acf2-4bec-9262-30d1961d40b1"
    };

    /** Names of the medicine categories. */
    private static final String[] CATEGORY_NAMES = {
            "Analgesic", "Antibiotic"
    };

    /** Batch numbers assigned to the sample stocks. */
    private static final String[] BATCH_NUMBERS = {"B-001", "B-002"};

    /** Expiration dates of the sample stocks. */
    private static final String[] EXPIRATION_DATES = {
            "2027-12-31", "2027-06-30"
    };

    /** Default quantity received for each sample stock. */
    private static final int QUANTITY_RECEIVED = 50;

    /** Expected status when the remaining stock is enough. */
    private static final String STATUS_ENOUGH = "Enough";

    /** Used to mock the static database connection method. */
    private MockedStatic<ConnectionHelper> connectionHelper;

    /** Mock database connection used by the DAO. */
    private Connection connection;

    /** Handles the medicine stock select queries. */
    private PreparedStatement statement;

    /** Holds the mocked medicine stock query results. */
    private ResultSet resultSet;

    /** Handles the query for the quantity already dispensed. */
    private PreparedStatement dispensedStatement;

    /** Contains the result of the dispensed quantity query. */
    private ResultSet dispensedResultSet;

    /** Handles updates made to medicine stock records. */
    private PreparedStatement updateStatement;

    /** DAO being tested in this class. */
    private MedicineStockDaoImpl dao;

    @BeforeEach
    void setUp() throws SQLException {

        // Create mock objects before running each test.
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        dispensedStatement = mock(PreparedStatement.class);
        dispensedResultSet = mock(ResultSet.class);
        updateStatement = mock(PreparedStatement.class);

        // Make ConnectionHelper return the mocked connection.
        connectionHelper = mockStatic(ConnectionHelper.class);
        connectionHelper.when(ConnectionHelper::getConnection)
                .thenReturn(connection);

        // Return the appropriate mock statement for each query.
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(connection.prepareStatement(startsWith("select coalesce")))
                .thenReturn(dispensedStatement);
        when(connection.prepareStatement(startsWith("update")))
                .thenReturn(updateStatement);

        // Set the default results for the mocked database operations.
        when(statement.executeQuery()).thenReturn(resultSet);
        when(dispensedStatement.executeQuery()).thenReturn(dispensedResultSet);
        when(updateStatement.executeUpdate()).thenReturn(1);

        // Create a new DAO instance for the current test.
        dao = new MedicineStockDaoImpl();
    }

    @AfterEach
    void tearDown() {

        // Close the static mock so it will not affect other tests.
        connectionHelper.close();
    }

    @Test
    @DisplayName("RCISD-5-TC1, TC2, TC4: all medicine stocks with details")
    void findAllReturnsEveryMedicineStockWithItsMedicineDetails()
            throws SQLException {

        // Prepare two sample medicine stock records.
        stubRows(PARACETAMOL, AMOXICILLIN);

        // Retrieve all medicine stocks.
        List<MedicineStock> stocks = dao.findAll();

        // Check that both records were returned with the correct details.
        assertEquals(2, stocks.size());
        assertStock(stocks.get(0), PARACETAMOL);
        assertStock(stocks.get(1), AMOXICILLIN);
    }

    @Test
    @DisplayName("RCISD-5-TC7: no medicine records")
    void findAllReturnsEmptyListWhenNoMedicineRecordsExistOrDatabaseFails()
            throws SQLException {

        // Return an empty result set.
        stubRows();

        // No records should be returned when the table has no matching data.
        assertTrue(dao.findAll().isEmpty());

        // Simulate a database connection error.
        when(connection.prepareStatement(anyString()))
                .thenThrow(new SQLException("connection failed"));

        // Call findAll again while the database is unavailable.
        List<MedicineStock> onFailure = dao.findAll();

        // The method should return an empty list instead of null.
        assertNotNull(onFailure);
        assertTrue(onFailure.isEmpty());
    }

    @Test
    @DisplayName("RCISD-5-TC3: details of the selected medicine stock")
    void findByIdReturnsStockWithMedicineDetailsForTheGivenId()
            throws SQLException {

        // Prepare the result for the selected stock.
        stubRows(PARACETAMOL);

        // Look up the stock using its ID.
        MedicineStock stock = dao.findById(STOCK_IDS[PARACETAMOL]);

        // Make sure the correct stock and its medicine details are returned.
        assertNotNull(stock);
        assertStock(stock, PARACETAMOL);

        // Check that the ID was passed to the prepared statement.
        verify(statement).setString(1, STOCK_IDS[PARACETAMOL]);
    }

    @Test
    void findByIdLeavesExpirationDateAndCategoryEmptyWhenNotRecorded()
            throws SQLException {

        // Prepare a stock without an expiration date or category.
        stubRows(PARACETAMOL);
        when(resultSet.getDate("expiration_date")).thenReturn((Date) null);
        when(resultSet.getString("category_id")).thenReturn((String) null);

        // Retrieve the stock with the missing optional details.
        MedicineStock stock = dao.findById(STOCK_IDS[PARACETAMOL]);

        // The stock and medicine should still be created successfully.
        assertNotNull(stock);
        assertNull(stock.getExpirationDate());
        assertNotNull(stock.getMedicine());

        // A missing category should remain null.
        assertNull(stock.getMedicine().getCategory());
    }

    @Test
    void findByIdReturnsNullWhenStockIsNotFoundOrDatabaseFails()
            throws SQLException {

        // Return no records to simulate an ID that does not exist.
        stubRows();

        // No stock should be returned for a missing ID.
        assertNull(dao.findById("missing"));

        // Make sure the requested ID was used in the query.
        verify(statement).setString(1, "missing");

        // Simulate an error while executing the select query.
        when(statement.executeQuery()).thenThrow(new SQLException("failed"));

        // The DAO should return null when the query fails.
        assertNull(dao.findById(STOCK_IDS[PARACETAMOL]));
    }

    @Test
    @DisplayName("RCISD-5-TC5: search using the full medicine name")
    void searchReturnsTheMatchingStockWhenTheFullMedicineNameIsGiven()
            throws SQLException {

        // Prepare a matching Amoxicillin record.
        stubRows(AMOXICILLIN);

        // Search using the complete medicine name.
        List<MedicineStock> stocks = dao.search("Amoxicillin 500mg");

        // Only the matching stock should be returned.
        assertEquals(1, stocks.size());
        assertStock(stocks.get(0), AMOXICILLIN);

        // Check that the search keyword was converted into a LIKE pattern.
        verify(statement).setString(1, "%amoxicillin 500mg%");
    }

    @Test
    @DisplayName("RCISD-5-TC6: search using a keyword")
    void searchReturnsTheMatchingStockWhenAKeywordIsGivenIgnoringCase()
            throws SQLException {

        // Prepare the Paracetamol record for the first search.
        stubRows(PARACETAMOL);

        // Search using only part of the medicine name.
        List<MedicineStock> stocks = dao.search("Paracetamol");

        // Check the returned record.
        assertEquals(1, stocks.size());
        assertStock(stocks.get(0), PARACETAMOL);

        // Prepare the result again for a search with different letter casing.
        stubRows(PARACETAMOL);

        // Leading and trailing spaces should not affect the search.
        assertEquals(1, dao.search("  PARACETAMOL ").size());

        // Both searches should use the same lowercase search pattern.
        verify(statement, times(2)).setString(1, "%paracetamol%");
    }

    @Test
    void searchReturnsAllStocksWhenTheKeywordIsBlank() throws SQLException {

        // Prepare both stock records.
        stubRows(PARACETAMOL, AMOXICILLIN);

        // A blank keyword should return all available stocks.
        assertEquals(2, dao.search("   ").size());

        // Prepare the records again for a null keyword.
        stubRows(PARACETAMOL, AMOXICILLIN);

        // A null keyword should also return all stocks.
        assertEquals(2, dao.search(null).size());

        // No search parameter should be added when the keyword is empty.
        verify(statement, never()).setString(anyInt(), anyString());
    }

    @Test
    void searchReturnsEmptyListWhenNothingMatchesOrDatabaseFails()
            throws SQLException {

        // Return an empty result when no medicine matches the keyword.
        stubRows();

        assertTrue(dao.search("Ibuprofen").isEmpty());

        // Simulate a database error during the search.
        when(statement.executeQuery()).thenThrow(new SQLException("failed"));

        // Try searching again while the query is failing.
        List<MedicineStock> onFailure = dao.search("Ibuprofen");

        // The method should return an empty list on failure.
        assertNotNull(onFailure);
        assertTrue(onFailure.isEmpty());
    }

    @Test
    void findLowStockReturnsStocksUsingTheLowStockLevel()
            throws SQLException {

        // Prepare a sample stock record for the low stock query.
        stubRows(AMOXICILLIN);

        // Retrieve stocks that meet the low stock condition.
        List<MedicineStock> stocks = dao.findLowStock();

        // Check the returned stock and the quantity threshold used.
        assertEquals(1, stocks.size());
        assertStock(stocks.get(0), AMOXICILLIN);
        verify(statement).setInt(1, 10);
    }

    @Test
    void findLowStockReturnsEmptyListWhenNoStockIsLowOrDatabaseFails()
            throws SQLException {

        // Return no records when there are no low stocks.
        stubRows();

        assertTrue(dao.findLowStock().isEmpty());

        // Simulate a failed query.
        when(statement.executeQuery()).thenThrow(new SQLException("failed"));

        // Run the low stock query again while the database is failing.
        List<MedicineStock> onFailure = dao.findLowStock();

        // The DAO should return an empty list instead of null.
        assertNotNull(onFailure);
        assertTrue(onFailure.isEmpty());
    }

    @Test
    void updateQuantityRejectsInvalidQuantitiesButAcceptsEqualToDispensed()
            throws SQLException {

        // Use the Paracetamol stock for the quantity update.
        String id = STOCK_IDS[PARACETAMOL];

        // Negative quantities must not be accepted.
        assertThrows(IllegalArgumentException.class,
                () -> dao.updateQuantity(id, -1));

        // The database connection should not be requested for invalid input.
        connectionHelper.verify(ConnectionHelper::getConnection, never());

        // Set the quantity already dispensed to 20.
        stubDispensed(20);

        // A quantity below the dispensed amount should also be rejected.
        assertThrows(IllegalArgumentException.class,
                () -> dao.updateQuantity(id, 19));

        // No update query should run when the quantity is invalid.
        verify(connection, never()).prepareStatement(startsWith("update"));

        // A quantity equal to the dispensed amount is allowed.
        assertTrue(dao.updateQuantity(id, 20));
        verify(updateStatement).setInt(1, 20);
    }

    @Test
    void updateQuantitySavesQuantityAndDerivesStatusFromRemainingStock()
            throws SQLException {

        // Select the stock record and set the quantity already dispensed.
        String id = STOCK_IDS[PARACETAMOL];
        stubDispensed(5);

        // Remaining stock of 10 should be marked as low.
        assertTrue(dao.updateQuantity(id, 15));
        verify(updateStatement).setInt(1, 15);
        verify(updateStatement).setString(2, "Low Stock");

        // Remaining stock above 10 should be marked as enough.
        assertTrue(dao.updateQuantity(id, 16));
        verify(updateStatement).setInt(1, 16);
        verify(updateStatement).setString(2, STATUS_ENOUGH);

        // Make sure both updates use the selected stock ID.
        verify(updateStatement, times(2)).setString(3, id);
    }

    @Test
    void updateQuantityTreatsMissingDispensedRowAsNothingDispensed()
            throws SQLException {

        // Simulate a query that does not return a dispensed quantity.
        when(dispensedResultSet.next()).thenReturn(false);

        // The update should continue with a dispensed quantity of zero.
        assertTrue(dao.updateQuantity(STOCK_IDS[PARACETAMOL], 50));

        // Check that the full quantity is saved and the status is enough.
        verify(updateStatement).setInt(1, 50);
        verify(updateStatement).setString(2, STATUS_ENOUGH);
    }

    @Test
    void updateQuantityReturnsFalseWhenNoRowIsUpdatedOrDatabaseFails()
            throws SQLException {

        // Set the dispensed quantity to zero.
        stubDispensed(0);

        // Simulate an update that does not affect any database row.
        when(updateStatement.executeUpdate()).thenReturn(0);

        assertFalse(dao.updateQuantity("missing", 50));

        // Simulate a database error while retrieving the dispensed quantity.
        when(dispensedStatement.executeQuery())
                .thenThrow(new SQLException("failed"));

        // The DAO should return false when the update cannot be completed.
        assertFalse(dao.updateQuantity(STOCK_IDS[PARACETAMOL], 50));
    }

    /**
     * Checks the stock details against the expected sample data.
     *
     * @param stock The stock record returned by the DAO.
     * @param row The index of the expected sample data.
     */
    private void assertStock(MedicineStock stock, int row) {

        // Check the basic stock information.
        assertEquals(STOCK_IDS[row], stock.getId());
        assertEquals(BATCH_NUMBERS[row], stock.getBatchNumber());
        assertEquals(QUANTITY_RECEIVED, stock.getQuantityReceived());
        assertEquals(LocalDate.parse(EXPIRATION_DATES[row]).atStartOfDay(),
                stock.getExpirationDate());
        assertEquals(STATUS_ENOUGH, stock.getStatus());

        // Check the medicine linked to the stock.
        Medicine medicine = stock.getMedicine();
        assertNotNull(medicine);
        assertEquals(MEDICINE_IDS[row], medicine.getId());
        assertEquals(MEDICINE_NAMES[row], medicine.getMedicineName());
        assertEquals(UNITS[row], medicine.getUnit());

        // Check the medicine category and its details.
        assertNotNull(medicine.getCategory());
        assertEquals(CATEGORY_IDS[row], medicine.getCategory().getId());
        assertEquals(CATEGORY_NAMES[row],
                medicine.getCategory().getCategoryName());
    }

    /**
     * Prepares the mocked result set using the selected sample records.
     *
     * @param rows The sample data indexes to include. Empty means no records.
     * @throws SQLException if setting up the mocked result set fails.
     */
    private void stubRows(int... rows) throws SQLException {

        // Set how many times the result set should return a record.
        OngoingStubbing<Boolean> hasNext = when(resultSet.next());
        for (int i = 0; i < rows.length; i++) {
            hasNext = hasNext.thenReturn(true);
        }

        // Return false once all sample records have been read.
        hasNext.thenReturn(false);

        // Set the values for each medicine stock column.
        stubColumn("id", STOCK_IDS, rows);
        stubColumn("batch_number", BATCH_NUMBERS, rows);
        stubColumn("medicine_id", MEDICINE_IDS, rows);
        stubColumn("medicine_name", MEDICINE_NAMES, rows);
        stubColumn("unit", UNITS, rows);
        stubColumn("category_id", CATEGORY_IDS, rows);
        stubColumn("category_name", CATEGORY_NAMES, rows);

        // Set the expiration date when sample records are provided.
        if (rows.length > 0) {
            OngoingStubbing<Date> expiration =
                    when(resultSet.getDate("expiration_date"));

            for (int row : rows) {
                expiration = expiration.thenReturn(
                        Date.valueOf(EXPIRATION_DATES[row]));
            }
        }

        // Use the default quantity and status for the sample records.
        when(resultSet.getInt("quantity_received"))
                .thenReturn(QUANTITY_RECEIVED);
        when(resultSet.getString("status")).thenReturn(STATUS_ENOUGH);
    }

    /**
     * Sets up a mocked column using the values from the selected sample rows.
     *
     * @param column The database column name.
     * @param values The sample values for that column.
     * @param rows The indexes of the records to include.
     * @throws SQLException if setting up the mocked column fails.
     */
    private void stubColumn(String column, String[] values, int... rows)
            throws SQLException {

        // There is nothing to configure when no records are returned.
        if (rows.length == 0) {
            return;
        }

        // Return the corresponding value for each selected row.
        OngoingStubbing<String> stub = when(resultSet.getString(column));
        for (int row : rows) {
            stub = stub.thenReturn(values[row]);
        }
    }

    /**
     * Sets the mocked result for the quantity already dispensed.
     *
     * @param dispensed The number of units already dispensed.
     * @throws SQLException if setting up the mocked result fails.
     */
    private void stubDispensed(int dispensed) throws SQLException {

        // Indicate that the query returned a record.
        when(dispensedResultSet.next()).thenReturn(true);

        // Return the specified dispensed quantity.
        when(dispensedResultSet.getInt(1)).thenReturn(dispensed);
    }
}
