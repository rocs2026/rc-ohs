package com.rocs.rc.ohs.app.facade.consultation.impl;

import com.rocs.rc.ohs.app.facade.consultation.ConsultationFacade;
import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.prescription.Prescription;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.consultation.ConsultationDao;
import com.rocs.rc.ohs.data.dao.consultation.impl.ConsultationDaoImpl;
import com.rocs.rc.ohs.data.dao.prescription.PrescriptionDao;
import com.rocs.rc.ohs.data.dao.prescription.impl.PrescriptionDaoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implements the consultation operations of the clinic system.
 */
public class ConsultationFacadeImpl implements ConsultationFacade {

    /** The data access object of the consultations. */
    private final ConsultationDao consultationDao;

    /** The data access object of the prescriptions. */
    private final PrescriptionDao prescriptionDao;

    /**
     * Creates a facade that uses the default data access objects.
     */
    public ConsultationFacadeImpl() {
        this(new ConsultationDaoImpl(), new PrescriptionDaoImpl());
    }

    /**
     * Creates a facade that uses the given data access objects.
     *
     * @param newConsultationDao The DAO of the consultations.
     * @param newPrescriptionDao The DAO of the prescriptions.
     */
    public ConsultationFacadeImpl(ConsultationDao newConsultationDao,
                                  PrescriptionDao newPrescriptionDao) {
        this.consultationDao = newConsultationDao;
        this.prescriptionDao = newPrescriptionDao;
    }

    /**
     * Saves a consultation together with its prescriptions as one
     * transaction. Nothing is saved if one of them fails.
     *
     * @param consultation The consultation to save.
     * @param prescriptions The prescriptions of the consultation.
     * @return The saved {@link Consultation} with its generated ID.
     * @throws IllegalArgumentException If a required value is missing.
     */
    @Override
    public Consultation saveConsultation(Consultation consultation,
                                         List<Prescription> prescriptions) {
        List<Prescription> items = prescriptions == null
                ? new ArrayList<>() : prescriptions;
        validate(consultation, items);
        consultation.setId(newId());
        try (Connection connection = ConnectionHelper.getConnection()) {
            connection.setAutoCommit(false);
            try {
                consultationDao.add(connection, consultation);
                for (Prescription prescription : items) {
                    prescription.setId(newId());
                    prescription.setConsultation(consultation);
                    prescription.setPersonnel(consultation.getStaff());
                    prescriptionDao.add(connection, prescription);
                }
                connection.commit();
            } catch (RuntimeException ex) {
                connection.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error saving the consultation.", ex);
        }
        return consultation;
    }

    /**
     * Retrieves the saved consultations of a patient, latest first.
     *
     * @param patientId The UUID of the patient.
     * @return A list of consultations of the patient.
     * @throws IllegalArgumentException If the patient ID is missing.
     */
    @Override
    public List<Consultation> getConsultationsByPatient(String patientId) {
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("The patient ID is required.");
        }
        return consultationDao.findByPatientId(patientId);
    }

    /**
     * Retrieves the prescriptions of a saved consultation.
     *
     * @param consultationId The UUID of the consultation.
     * @return A list of prescriptions of the consultation.
     * @throws IllegalArgumentException If the consultation ID is missing.
     */
    @Override
    public List<Prescription> getPrescriptionsByConsultation(
            String consultationId) {
        if (consultationId == null || consultationId.isBlank()) {
            throw new IllegalArgumentException(
                    "The consultation ID is required.");
        }
        return prescriptionDao.findByConsultationId(consultationId);
    }

    /**
     * Checks the business rules of a consultation: it needs a diagnosis
     * and every prescription needs a quantity above zero.
     *
     * @param consultation The consultation to check.
     * @param prescriptions The prescriptions to check.
     * @throws IllegalArgumentException If a rule is not met.
     */
    private void validate(Consultation consultation,
                          List<Prescription> prescriptions) {
        if (consultation == null || consultation.getDiagnosis() == null
                || consultation.getDiagnosis().isBlank()) {
            throw new IllegalArgumentException(
                    "The consultation and its diagnosis are required.");
        }
        for (Prescription prescription : prescriptions) {
            if (prescription == null
                    || prescription.getQuantityToDispense() <= 0) {
                throw new IllegalArgumentException(
                        "Each prescription needs a quantity above zero.");
            }
        }
    }

    /**
     * Generates a new UUID for a record.
     *
     * @return The new UUID as text.
     */
    private String newId() {
        return UUID.randomUUID().toString();
    }
}
