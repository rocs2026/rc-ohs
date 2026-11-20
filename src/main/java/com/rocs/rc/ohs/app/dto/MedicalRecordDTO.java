package com.rocs.rc.ohs.app.dto;

import java.time.LocalDateTime;

/**
 * Carries a patient and the patient's medical record to the screen.
 */
public class MedicalRecordDTO {

    /**
     * The ID of the patient.
     */
    private String patientId;

    /**
     * The first name of the patient.
     */
    private String firstName;

    /**
     * The middle name of the patient.
     */
    private String middleName;

    /**
     * The last name of the patient.
     */
    private String lastName;

    /**
     * The ID of the medical record.
     */
    private String medicalRecordId;

    /**
     * The medical conditions of the patient.
     */
    private String medicalConditions;

    /**
     * The allergies of the patient.
     */
    private String allergies;

    /**
     * The date of the consultation.
     */
    private LocalDateTime consultationDate;

    /**
     * The complaint raised in the consultation.
     */
    private String complaint;

    /**
     * The diagnosis made in the consultation.
     */
    private String diagnosis;

    /**
     * The treatment given in the consultation.
     */
    private String treatment;

    /**
     * Gets the ID of the patient.
     *
     * @return ID of the patient
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the ID of the patient.
     *
     * @param newPatientId the ID of the patient
     */
    public void setPatientId(String newPatientId) {
        this.patientId = newPatientId;
    }

    /**
     * Gets the first name of the patient.
     *
     * @return the first name of the patient
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the patient.
     *
     * @param newFirstName the first name of the patient
     */
    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }

    /**
     * Gets the middle name of the patient.
     *
     * @return the middle name of the patient
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the middle name of the patient.
     *
     * @param newMiddleName the middle name of the patient
     */
    public void setMiddleName(String newMiddleName) {
        this.middleName = newMiddleName;
    }

    /**
     * Gets the last name of the patient.
     *
     * @return the last name of the patient
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the patient.
     *
     * @param newLastName the last name of the patient
     */
    public void setLastName(String newLastName) {
        this.lastName = newLastName;
    }

    /**
     * Gets the ID of the medical record.
     *
     * @return the ID of the medical record
     */
    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    /**
     * Sets the ID of the medical record.
     *
     * @param newMedicalRecordId the ID of the medical record
     */
    public void setMedicalRecordId(String newMedicalRecordId) {
        this.medicalRecordId = newMedicalRecordId;
    }

    /**
     * Gets the medical conditions of the patient.
     *
     * @return the medical conditions of the patient
     */
    public String getMedicalConditions() {
        return medicalConditions;
    }

    /**
     * Sets the medical conditions of the patient.
     *
     * @param newMedicalConditions the medical conditions of the patient
     */
    public void setMedicalConditions(String newMedicalConditions) {
        this.medicalConditions = newMedicalConditions;
    }

    /**
     * Gets the allergies of the patient.
     *
     * @return the allergies of the patient
     */
    public String getAllergies() {
        return allergies;
    }

    /**
     * Sets the allergies of the patient.
     *
     * @param newAllergies the allergies of the patient
     */
    public void setAllergies(String newAllergies) {
        this.allergies = newAllergies;
    }

    /**
     * Gets the date of the consultation.
     *
     * @return the date of the consultation
     */
    public LocalDateTime getConsultationDate() {
        return consultationDate;
    }

    /**
     * Sets the date of the consultation.
     *
     * @param newConsultationDate the date of the consultation
     */
    public void setConsultationDate(LocalDateTime newConsultationDate) {
        this.consultationDate = newConsultationDate;
    }

    /**
     * Gets the complaint raised in the consultation.
     *
     * @return the complaint raised in the consultation
     */
    public String getComplaint() {
        return complaint;
    }

    /**
     * Sets the complaint raised in the consultation.
     *
     * @param newComplaint the complaint raised in the consultation
     */
    public void setComplaint(String newComplaint) {
        this.complaint = newComplaint;
    }

    /**
     * Gets the diagnosis made in the consultation.
     *
     * @return the diagnosis made in the consultation
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Sets the diagnosis made in the consultation.
     *
     * @param newDiagnosis the diagnosis made in the consultation
     */
    public void setDiagnosis(String newDiagnosis) {
        this.diagnosis = newDiagnosis;
    }

    /**
     * Gets the treatment given in the consultation.
     *
     * @return the treatment given in the consultation
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * Sets the treatment given in the consultation.
     *
     * @param newTreatment the treatment given in the consultation
     */
    public void setTreatment(String newTreatment) {
        this.treatment = newTreatment;
    }
}
