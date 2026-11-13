package com.rocs.rc.ohs.data.utils;

/**
 * SQL statement definitions used across data access classes.
 */
public final class QueryConstants {

    /** Query to search patient records across names, IDs, and numbers. */
    public static final String SEARCH_MEDICAL_RECORD =
            "SELECT p.id AS p_id, p.first_name, p.middle_name, "
                    + "p.last_name, mr.id AS mr_id, mr.medical_conditions, "
                    + "mr.allergies, c.id AS c_id, c.consultation_date, "
                    + "c.complaint, c.diagnosis, c.treatment "
                    + "FROM person p "
                    + "LEFT JOIN student s ON s.person_id = p.id "
                    + "LEFT JOIN personnel n ON n.person_id = p.id "
                    + "JOIN medical_record mr ON mr.patient_id = p.id "
                    + "LEFT JOIN consultation c ON c.id = mr.consultation_id "
                    + "WHERE LOWER(p.id) LIKE LOWER(?) "
                    + "OR LOWER(p.first_name) LIKE LOWER(?) "
                    + "OR LOWER(p.middle_name) LIKE LOWER(?) "
                    + "OR LOWER(p.last_name) LIKE LOWER(?) "
                    + "OR LOWER(s.student_number) LIKE LOWER(?) "
                    + "OR LOWER(n.employee_id) LIKE LOWER(?) "
                    + "ORDER BY p.last_name, p.first_name, "
                    + "c.consultation_date DESC";

    /** Query to update patient medical conditions and allergies. */
    public static final String UPDATE_MEDICAL_RECORD_STATEMENT =
            "UPDATE medical_record "
                    + "SET medical_conditions = ?, allergies = ? "
                    + "WHERE id = ?";

    private QueryConstants() {
    }
}
