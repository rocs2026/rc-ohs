package com.rocs.rc.ohs.data.utils;

/**
 * Holds the SQL queries used by the data access classes.
 */
public final class QueryConstants {

    /**
     * Searches a patient (student or personnel) by ID, name, student number
     * or employee ID and returns the patient's medical records with their
     * consultations.
     */
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

    /**
     * Prevents creating an instance of this utility class.
     */
    private QueryConstants() {
    }
}
