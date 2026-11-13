package com.rocs.rc.ohs.data.utils.medicalrecord;

/**
 * Holds the SQL statements used by the data access objects.
 */
public final class QueryConstants {

    /**
     * Prevents instantiation of this class.
     */
    private QueryConstants() {
    }

    /**
     * Updates the Medical record of a patient (med conditions,
     * allergies).
     */
    public static final String UPDATE_MEDICAL_RECORD_STATEMENT =
            "UPDATE medical_record "
                + "SET medical_conditions = ?, allergies = ? "
                + "WHERE id =?";

}
