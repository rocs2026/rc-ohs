package com.rocs.rc.ohs.app.model.prescription;

/**
 * Represents a medicine prescribed in a consultation.
 */
public class Prescription {

    /** The unique ID of the prescription. */
    private int id;

    /** The ID of the consultation. */
    private int consultationId;

    /** The ID of the medicine stock prescribed. */
    private int medicineStockId;

    /** The ID of the personnel who prescribed it. */
    private int personnelId;

    /** The quantity of the medicine to dispense. */
    private int quantityToDispense;

    /** The dose to take each time. */
    private String dose;

    /** How often the medicine is taken. */
    private String frequency;

    /** The additional instructions. */
    private String instructions;

    /**
     * Retrieves the unique ID of the prescription.
     *
     * @return the unique ID of the prescription.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of the prescription.
     *
     * @param newId the unique ID of the prescription.
     */
    public void setId(int newId) {
        this.id = newId;
    }

    /**
     * Retrieves the ID of the consultation.
     *
     * @return the ID of the consultation.
     */
    public int getConsultationId() {
        return consultationId;
    }

    /**
     * Sets the ID of the consultation.
     *
     * @param newConsultationId the ID of the consultation.
     */
    public void setConsultationId(int newConsultationId) {
        this.consultationId = newConsultationId;
    }

    /**
     * Retrieves the ID of the medicine stock prescribed.
     *
     * @return the ID of the medicine stock prescribed.
     */
    public int getMedicineStockId() {
        return medicineStockId;
    }

    /**
     * Sets the ID of the medicine stock prescribed.
     *
     * @param newMedicineStockId the ID of the medicine stock prescribed.
     */
    public void setMedicineStockId(int newMedicineStockId) {
        this.medicineStockId = newMedicineStockId;
    }

    /**
     * Retrieves the ID of the personnel who prescribed it.
     *
     * @return the ID of the personnel who prescribed it.
     */
    public int getPersonnelId() {
        return personnelId;
    }

    /**
     * Sets the ID of the personnel who prescribed it.
     *
     * @param newPersonnelId the ID of the personnel who prescribed it.
     */
    public void setPersonnelId(int newPersonnelId) {
        this.personnelId = newPersonnelId;
    }

    /**
     * Retrieves the quantity of the medicine to dispense.
     *
     * @return the quantity of the medicine to dispense.
     */
    public int getQuantityToDispense() {
        return quantityToDispense;
    }

    /**
     * Sets the quantity of the medicine to dispense.
     *
     * @param newQuantityToDispense the quantity of the medicine to dispense.
     */
    public void setQuantityToDispense(int newQuantityToDispense) {
        this.quantityToDispense = newQuantityToDispense;
    }

    /**
     * Retrieves the dose to take each time.
     *
     * @return the dose to take each time.
     */
    public String getDose() {
        return dose;
    }

    /**
     * Sets the dose to take each time.
     *
     * @param newDose the dose to take each time.
     */
    public void setDose(String newDose) {
        this.dose = newDose;
    }

    /**
     * Retrieves how often the medicine is taken.
     *
     * @return how often the medicine is taken.
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets how often the medicine is taken.
     *
     * @param newFrequency how often the medicine is taken.
     */
    public void setFrequency(String newFrequency) {
        this.frequency = newFrequency;
    }

    /**
     * Retrieves the additional instructions.
     *
     * @return the additional instructions.
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * Sets the additional instructions.
     *
     * @param newInstructions the additional instructions.
     */
    public void setInstructions(String newInstructions) {
        this.instructions = newInstructions;
    }
}
