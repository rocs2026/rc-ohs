package com.rocs.rc.ohs.app.facade.consultation;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.prescription.Prescription;

import java.util.List;

/**
 * Defines the consultation operations of the clinic system.
 */
public interface ConsultationFacade {

    /**
     * Saves a consultation together with its prescriptions. Everything is
     * checked first, so nothing is saved when a required value is missing.
     *
     * @param consultation The consultation to save.
     * @param prescriptions The prescriptions of the consultation.
     * @return The saved {@link Consultation} with its generated ID.
     */
    Consultation saveConsultation(Consultation consultation,
                                  List<Prescription> prescriptions);

    /**
     * Retrieves the saved consultations of a patient, latest first.
     *
     * @param patientId The UUID of the patient.
     * @return A list of consultations of the patient.
     */
    List<Consultation> getConsultationsByPatient(String patientId);

    /**
     * Retrieves the prescriptions of a saved consultation.
     *
     * @param consultationId The UUID of the consultation.
     * @return A list of prescriptions of the consultation.
     */
    List<Prescription> getPrescriptionsByConsultation(
            String consultationId);
}
