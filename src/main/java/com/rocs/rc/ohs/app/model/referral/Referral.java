package com.rocs.rc.ohs.app.model.referral;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.app.model.personnel.Personnel;

/**
 * Represents a referral issued to a patient in the clinic.
 */
public class Referral {

    /** The unique ID of the referral. */
    private String id;

    /** The patient who is given a referral, null if there's none. */
    private Person patient;

    /** The consultation of the referral, null if there is none. */
    private Consultation consultation;

    /** The personnel who issued the referral*/
    private Personnel personnel;

    /** The hospital or facility the patient is referred to., null if there is none.*/
    private String referredTo;

    /** The reason for the referral. */
    private String reasonFor;

    /**
     * Retrieves the unique ID of referral.
     *
     * @return the unique ID of the referral.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique ID of the referral.
     *
     * @param newId the unique ID of the referral.
     */
    public void setId(String newId) {
        this.id = newId;
    }

    /**
     * Retrieves the patient associated with the referral.
     *
     * @return the patient associated with the referral.
     */
    public Person getPatient() {
        return patient;
    }

    /**
     * Sets the patient associated with the referral.
     *
     * @param newPatient the patient associated with the referral.
     */
    public void setPatient(Person newPatient) {
        this.patient = newPatient;
    }

    /**
     * Retrieves the consultation associated with the referral.
     *
     * @return the consultation associated with the referral.
     */
    public Consultation getConsultation() {
        return consultation;
    }

    /**
     * Sets the consultation associated with the referral.
     *
     * @param newConsultation the consultation associated with the referral.
     */
    public void setConsultation(Consultation newConsultation) {
        this.consultation = newConsultation;
    }

    /**
     * Retrieves the personnel who issued the referral.
     *
     * @return the personnel who issued the referral.
     */
    public Personnel getPersonnel() {
        return personnel;
    }

    /**
     * Sets the personnel who issued the referral.
     *
     * @param newPersonnel the personnel who issued the referral.
     */
    public void setPersonnel(Personnel newPersonnel) {
        this.personnel = newPersonnel;
    }

    /**
     * Retrieves the hospital or facility the patient is referred to.
     *
     * @return the hospital or facility the patient is referred to.
     */
    public String getReferredTo() {
        return referredTo;
    }

    /**
     * Sets the hospital or facility the patient is referred to.
     *
     * @param newReferredTo the hospital or facility the patient is referred to.
     */
    public void setReferredTo(String newReferredTo) {
        this.referredTo = newReferredTo;
    }

    /**
     * Retrieves the reason for the referral.
     *
     * @return the reason for the referral.
     */
    public String getReasonFor() {
        return reasonFor;
    }

    /**
     * Sets the reason for the referral.
     *
     * @param newReasonFor the reason for the referral.
     */
    public void setReasonFor(String newReasonFor) {
        this.reasonFor = newReasonFor;
    }
}
