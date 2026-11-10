package com.rocs.rc.ohs.data.dao.prescription;

import com.rocs.rc.ohs.app.model.prescription.Prescription;

import java.sql.Connection;
import java.util.List;

/**
 * Defines the data access operations for prescriptions.
 */
public interface PrescriptionDao {

    /**
     * Saves a new prescription using the given connection, so the caller
     * can save it together with other records in one transaction.
     *
     * @param connection The connection to use. It is not closed here.
     * @param prescription The prescription to save. Its ID must be set.
     * @return The saved {@link Prescription}.
     */
    Prescription add(Connection connection, Prescription prescription);

    /**
     * Retrieves all prescriptions issued in a consultation, with the name
     * and unit of each medicine, so they can be shown in the patient's
     * medical records.
     *
     * @param consultationId The UUID of the consultation.
     * @return A list of prescriptions of the consultation.
     */
    List<Prescription> findByConsultationId(String consultationId);
}
