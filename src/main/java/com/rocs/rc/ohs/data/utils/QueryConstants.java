package com.rocs.rc.ohs.data.utils;

/**
 * SQL statement definitions used across data access classes.
 */
public final class QueryConstants {

    /**
     * Query to search patient records across names, IDs, and numbers.
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
     * Updates the Medical record of a patient (med conditions, allergies).
     */
    public static final String UPDATE_MEDICAL_RECORD_STATEMENT =
            "UPDATE medical_record "
                    + "SET medical_conditions = ?, allergies = ? "
                    + "WHERE id = ?";

    /**
     * Saves a consultation when the clinic staff records a patient's
     * consultation with its diagnosis and treatment plan.
     */
    public static final String ADD_CONSULTATION_STATEMENT =
            "INSERT INTO consultation (id, patient_id, staff_id, "
                    + "record_type, consultation_date, complaint, "
                    + "diagnosis, treatment, action_taken, remarks) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Retrieves the saved consultations of a patient, latest first.
     */
    public static final String GET_CONSULTATIONS_BY_PATIENT_STATEMENT =
            "SELECT id, staff_id, record_type, consultation_date, "
                    + "complaint, diagnosis, treatment, action_taken, "
                    + "remarks FROM consultation WHERE patient_id = ? "
                    + "ORDER BY consultation_date DESC";

    /**
     * Saves a prescription under a consultation.
     */
    public static final String ADD_PRESCRIPTION_STATEMENT =
            "INSERT INTO prescription (id, consultation_id, "
                    + "medicine_stock_id, personnel_id, "
                    + "quantity_to_dispense, dose, frequency, "
                    + "instructions) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Retrieves the prescriptions of a consultation with the name and
     * unit of each medicine.
     */
    public static final String GET_PRESCRIPTIONS_BY_CONSULTATION_STATEMENT =
            "SELECT p.id, p.medicine_stock_id, p.personnel_id, "
                    + "p.quantity_to_dispense, p.dose, p.frequency, "
                    + "p.instructions, m.medicine_name, m.unit "
                    + "FROM prescription p "
                    + "JOIN medicine_stock ms ON ms.id = p.medicine_stock_id "
                    + "JOIN medicine m ON m.id = ms.medicine_id "
                    + "WHERE p.consultation_id = ? ORDER BY p.id";

    /**
     * Selects the login account that has the given username.
     */
    public static final String SELECT_LOGIN_BY_USERNAME =
            "select * from login where username = ?";

    /**
     * Updates the last login date of a login account.
     */
    public static final String UPDATE_LAST_LOGIN_DATE =
            "update login set last_login_date = ? where id = ?";

    /**
     * Updates the password of the login account that matches the
     * given username and the email of its personnel.
     */
    public static final String UPDATE_PASSWORD_BY_EMAIL_AND_USERNAME =
            "update login set password = ? "
                    + "where username = ? "
                    + "and person_id in (select pe.id from person pe "
                    + "join personnel p on p.person_id = pe.id "
                    + "where lower(pe.email) = lower(?))";

    private QueryConstants() {
    }
}
