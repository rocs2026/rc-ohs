package com.rocs.rc.ohs.data.utils.medicine;

/**
 * it is the query constants class for medicine.
 */
public final class QueryConstants {

    private QueryConstants() {
        // Private constructor to prevent instantiation
    }

    /**
     * it is the query to select all medicine.
     */
    public static final String SELECT_ALL_MEDICINE =
            "SELECT medicine.id, medicine.medicine_name, "
                    + "medicine.unit, medicine.category_id, "
                    + "medicine_category.category_name "
                    + "FROM medicine "
                    + "LEFT JOIN medicine_category "
                    + "ON medicine.category_id = medicine_category.id "
                    + "ORDER BY medicine.medicine_name;";

    /**
     * it is the query to select medicine by id.
     */
    public static final String SELECT_MEDICINE_BY_ID =
            "SELECT medicine.id, medicine.medicine_name, "
                    + "medicine.unit, medicine.category_id, "
                    + "medicine_category.category_name "
                    + "FROM medicine "
                    + "LEFT JOIN medicine_category "
                    + "ON medicine.category_id = medicine_category.id "
                    + "WHERE medicine.id = ?;";

    /**
     * it is the query to search medicine.
     */
    public static final String SEARCH_MEDICINE =
            "SELECT medicine.id, medicine.medicine_name, "
                    + "medicine.unit, medicine.category_id, "
                    + "medicine_category.category_name "
                    + "FROM medicine "
                    + "LEFT JOIN medicine_category "
                    + "ON medicine.category_id = medicine_category.id "
                    + "WHERE medicine.medicine_name LIKE ? "
                    + "ORDER BY medicine.medicine_name;";

    /**
     * it is the query to select all medicine stock.
     */
    public static final String SELECT_ALL_MEDICINE_STOCK =
            "SELECT medicine_stock_id, medicine_id, medicine_name, "
                    + "category_name, unit, batch_number, "
                    + "quantity_received, remaining_quantity, "
                    + "expiration_date, status "
                    + "FROM inventory_view "
                    + "ORDER BY medicine_name";

    /**
     * it is the query to search medicine stock.
     */
    public static final String SEARCH_MEDICINE_STOCK =
            "SELECT medicine_stock_id, medicine_id, medicine_name, "
                    + "category_name, unit, batch_number, "
                    + "quantity_received, remaining_quantity, "
                    + "expiration_date, status "
                    + "FROM inventory_view "
                    + "WHERE medicine_name LIKE ? "
                    + "OR category_name LIKE ? "
                    + "ORDER BY medicine_name";
}
