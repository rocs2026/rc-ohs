package com.rocs.rc.ohs.data.utils;

/**
 * Holds the SQL statements used by the data access objects.
 */
public final class QueryConstants {

    private QueryConstants() {
        // Private constructor to prevent instantiation
    }

    /**
     * Updates the Medical record of a patient (med conditions, allergies).
     */
    public static final String UPDATE_MEDICAL_RECORD_STATEMENT =
            "UPDATE medical_record "
                    + "SET medical_conditions = ?, allergies = ? "
                    + "WHERE id =?";

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
                    + "ORDER BY medicine.medicine_name";

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
                    + "WHERE medicine.id = ?";

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
                    + "ORDER BY medicine.medicine_name";

    /**
     * This is the function created by Wyne.
     */
    public static final String SELECT_ALL_MEDICINE_STOCK =
            "SELECT ms.id AS medicine_stock_id, m.id AS medicine_id, "
                    + "m.medicine_name, mc.category_name, m.unit, "
                    + "ms.batch_number, ms.quantity_received, "
                    + "fn_remaining_stock(ms.id) AS remaining_quantity, "
                    + "ms.expiration_date, ms.status "
                    + "FROM medicine_stock ms "
                    + "JOIN medicine m ON m.id = ms.medicine_id "
                    + "LEFT JOIN medicine_category mc "
                    + "ON mc.id = m.category_id "
                    + "ORDER BY m.medicine_name";

    /**
     * This is the function created by Wyne.
     */
    public static final String SEARCH_MEDICINE_STOCK =
            "SELECT ms.id AS medicine_stock_id, m.id AS medicine_id, "
                    + "m.medicine_name, mc.category_name, m.unit, "
                    + "ms.batch_number, ms.quantity_received, "
                    + "fn_remaining_stock(ms.id) AS remaining_quantity, "
                    + "ms.expiration_date, ms.status "
                    + "FROM medicine_stock ms "
                    + "JOIN medicine m ON m.id = ms.medicine_id "
                    + "LEFT JOIN medicine_category mc "
                    + "ON mc.id = m.category_id "
                    + "WHERE m.medicine_name LIKE ? "
                    + "OR mc.category_name LIKE ? "
                    + "ORDER BY m.medicine_name";

    /**
     * the remaining quantity of a medicine stock.
     */
    public static final String SELECT_REMAINING_STOCK =
            "SELECT fn_remaining_stock(?) AS remaining_quantity FROM dual";
}
