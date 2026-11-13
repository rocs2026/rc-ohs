package com.rocs.rc.ohs.data.utils;

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
}
