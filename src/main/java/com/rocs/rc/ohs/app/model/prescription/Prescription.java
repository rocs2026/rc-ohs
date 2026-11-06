package com.rocs.rc.ohs.app.model.prescription;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.app.model.personnel.Personnel;

/**
 * Represents a medicine prescribed in a consultation.
 * Represents a medicine prescribed during a consultation.
 */
public class Prescription {

    /** The unique ID of the prescription. */
    private Long id;

    /** The ID of the consultation. */
    private Long consultationId;
    /** The consultation associated with the prescription. */
    private Consultation consultation;

    /** The ID of the medicine stock prescribed. */
    private Long medicineStockId;
    /** The medicine stock associated with the prescription. */
    private MedicineStock medicineStock;

    /** The ID of the personnel who prescribed it. */
    private Long personnelId;
    /** The personnel who prescribed the medicine. */
    private Personnel personnel;

    /** The quantity of the medicine to dispense. */
    /** The quantity of medicine to dispense. */
    private int quantityToDispense;

    /** The dose to take each time. */
    /** The prescribed dose. */
    private String dose;

    /** How often the medicine should be taken. */
    /** How often the medicine is taken. */
    private String frequency;

    /** Additional instructions for the patient. */
    /** The additional instructions. */
    private String instructions;

    /**
     * Retrieves the unique ID of the prescription.
     *
     * @return the unique ID of the prescription.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the prescription.
     *
     * @param newId the unique ID of the prescription.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Retrieves the ID of the consultation.
     * Retrieves the consultation associated with the prescription.
     *
     * @return the ID of the consultation.
     * @return the consultation associated with the prescription.
     */
    public Long getConsultationId() {
        return consultationId;
    public Consultation getConsultation() {
        return consultation;
    }

    /**
     * Sets the ID of the consultation.
     * Sets the consultation associated with the prescription.
     *
     * @param newConsultationId the ID of the consultation.
     * @param newConsultation the consultation associated with the prescription.
     */
    public void setConsultationId(Long newConsultationId) {
        this.consultationId = newConsultationId;
    public void setConsultation(Consultation newConsultation) {
        this.consultation = newConsultation;
    }

    /**
     * Retrieves the ID of the medicine stock prescribed.
     * Retrieves the medicine stock associated with the prescription.
     *
     * @return the ID of the medicine stock prescribed.
     * @return the medicine stock associated with the prescription.
     */
    public Long getMedicineStockId() {
        return medicineStockId;
    public MedicineStock getMedicineStock() {
        return medicineStock;
    }

    /**
     * Sets the medicine stock associated with the prescription.
     * Sets the ID of the medicine stock prescribed.
     *
     * @param newMedicineStock the medicine stock associated with the
     *                         prescription.
     * @param newMedicineStockId the ID of the medicine stock prescribed.
     */
    public void setMedicineStock(MedicineStock newMedicineStock) {
        this.medicineStock = newMedicineStock;
    public void setMedicineStockId(Long newMedicineStockId) {
        this.medicineStockId = newMedicineStockId;
    }

    /**
     * Retrieves the ID of the personnel who prescribed it.
     * Retrieves the personnel who prescribed the medicine.
     *
     * @return the ID of the personnel who prescribed it.
     * @return the personnel who prescribed the medicine.
     */
    public Long getPersonnelId() {
        return personnelId;
    public Personnel getPersonnel() {
        return personnel;
    }

    /**
     * Sets the personnel who prescribed the medicine.
     * Sets the ID of the personnel who prescribed it.
     *
     * @param newPersonnel the personnel who prescribed the medicine.
     * @param newPersonnelId the ID of the personnel who prescribed it.
     */
    public void setPersonnel(Personnel newPersonnel) {
        this.personnel = newPersonnel;
    public void setPersonnelId(Long newPersonnelId) {
        this.personnelId = newPersonnelId;
    }

    /**
     * Retrieves the quantity of the medicine to dispense.
     * Retrieves the quantity of medicine to dispense.
     *
     * @return the quantity of the medicine to dispense.
     * @return the quantity of medicine to dispense.
     */
    public int getQuantityToDispense() {
        return quantityToDispense;
    }

    /**
     * Sets the quantity of medicine to dispense.
     * Sets the quantity of the medicine to dispense.
     *
     * @param newQuantityToDispense the quantity of medicine to dispense.
     * @param newQuantityToDispense the quantity of the medicine to dispense.
     */
    public void setQuantityToDispense(int newQuantityToDispense) {
        this.quantityToDispense = newQuantityToDispense;
    }

    /**
     * Retrieves the dose to take each time.
     * Retrieves the prescribed dose.
     *
     * @return the dose to take each time.
     * @return the prescribed dose.
     */
    public String getDose() {
        return dose;
    }

    /**
     * Sets the dose to take each time.
     * Sets the prescribed dose.
     *
     * @param newDose the dose to take each time.
     * @param newDose the prescribed dose.
     */
    public void setDose(String newDose) {
        this.dose = newDose;
    }

    /**
     * Retrieves how often the medicine is taken.
     * Retrieves how often the medicine should be taken.
     *
     * @return how often the medicine is taken.
     * @return how often the medicine should be taken.
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets how often the medicine should be taken.
     * Sets how often the medicine is taken.
     *
     * @param newFrequency how often the medicine should be taken.
     * @param newFrequency how often the medicine is taken.
     */
    public void setFrequency(String newFrequency) {
        this.frequency = newFrequency;
    }

    /**
     * Retrieves the additional instructions.
     * Retrieves the additional instructions for the patient.
     *
     * @return the additional instructions.
     * @return the additional instructions for the patient.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the additional instructions.
     * Sets the additional instructions for the patient.
     *
     * @param newInstructions the additional instructions.
     * @param newInstructions the additional instructions for the patient.
     */
    public void setInstructions(String newInstructions) {
        this.instructions = newInstructions;
    }
}
