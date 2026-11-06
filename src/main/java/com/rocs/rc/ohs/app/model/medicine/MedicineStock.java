package com.rocs.rc.ohs.app.model.medicine;

import com.rocs.rc.ohs.app.model.personnel.Personnel;

import java.time.LocalDateTime;

/**
 * Represents a medicine stock batch in the clinic.
 * Represents a stock of a medicine in the clinic.
 */
public class MedicineStock {

    /** The unique ID of the medicine stock. */
    private Long id;

    /** The ID of the medicine. */
    private Long medicineId;
    /** The medicine associated with the stock. */
    private Medicine medicine;

    /** The ID of the personnel who received it. */
    private Long personnelId;
    /** The personnel who recorded the medicine stock. */
    private Personnel personnel;

    /** The batch number of the stock. */
    /** The batch number of the medicine. */
    private String batchNumber;

    /** The quantity received in the stock. */
    /** The quantity of medicine received. */
    private int quantityReceived;

    /** The expiration date of the medicine. */
    /** The expiration date of the stock. */
    private LocalDateTime expirationDate;

    /** The current status of the medicine stock. */
    /** The status of the stock. */
    private String status;

    /**
     * Retrieves the unique ID of the medicine stock.
     *
     * @return the unique ID of the medicine stock.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the medicine stock.
     *
     * @param newId the unique ID of the medicine stock.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Retrieves the medicine associated with the stock.
     * Retrieves the ID of the medicine.
     *
     * @return the medicine associated with the stock.
     * @return the ID of the medicine.
     */
    public Medicine getMedicine() {
        return medicine;
    public Long getMedicineId() {
        return medicineId;
    }

    /**
     * Sets the ID of the medicine.
     * Sets the medicine associated with the stock.
     *
     * @param newMedicineId the ID of the medicine.
     * @param newMedicine the medicine associated with the stock.
     */
    public void setMedicineId(Long newMedicineId) {
        this.medicineId = newMedicineId;
    public void setMedicine(Medicine newMedicine) {
        this.medicine = newMedicine;
    }

    /**
     * Retrieves the personnel who recorded the medicine stock.
     * Retrieves the ID of the personnel who received it.
     *
     * @return the personnel who recorded the medicine stock.
     * @return the ID of the personnel who received it.
     */
    public Personnel getPersonnel() {
        return personnel;
    public Long getPersonnelId() {
        return personnelId;
    }

    /**
     * Sets the ID of the personnel who received it.
     * Sets the personnel who recorded the medicine stock.
     *
     * @param newPersonnelId the ID of the personnel who received it.
     * @param newPersonnel the personnel who recorded the medicine stock.
     */
    public void setPersonnelId(Long newPersonnelId) {
        this.personnelId = newPersonnelId;
    public void setPersonnel(Personnel newPersonnel) {
        this.personnel = newPersonnel;
    }

    /**
     * Retrieves the batch number of the stock.
     * Retrieves the batch number of the medicine.
     *
     * @return the batch number of the stock.
     * @return the batch number of the medicine.
     */
    public String getBatchNumber() {
        return batchNumber;
    }

    /**
     * Sets the batch number of the medicine.
     * Sets the batch number of the stock.
     *
     * @param newBatchNumber the batch number of the stock.
     * @param newBatchNumber the batch number of the medicine.
     */
    public void setBatchNumber(String newBatchNumber) {
        this.batchNumber = newBatchNumber;
    }

    /**
     * Retrieves the quantity of medicine received.
     * Retrieves the quantity received in the stock.
     *
     * @return the quantity of medicine received.
     * @return the quantity received in the stock.
     */
    public int getQuantityReceived() {
        return quantityReceived;
    }

    /**
     * Sets the quantity received in the stock.
     * Sets the quantity of medicine received.
     *
     * @param newQuantityReceived the quantity received in the stock.
     * @param newQuantityReceived the quantity of medicine received.
     */
    public void setQuantityReceived(int newQuantityReceived) {
        this.quantityReceived = newQuantityReceived;
    }

    /**
     * Retrieves the expiration date of the stock.
     * Retrieves the expiration date of the medicine.
     *
     * @return the expiration date of the stock.
     * @return the expiration date of the medicine.
     */
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expiration date of the medicine.
     * Sets the expiration date of the stock.
     *
     * @param newExpirationDate the expiration date of the medicine.
     * @param newExpirationDate the expiration date of the stock.
     */
    public void setExpirationDate(LocalDateTime newExpirationDate) {
        this.expirationDate = newExpirationDate;
    }

    /**
     * Retrieves the status of the stock.
     * Retrieves the current status of the medicine stock.
     *
     * @return the status of the stock.
     * @return the current status of the medicine stock.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the medicine stock.
     * Sets the status of the stock.
     *
     * @param newStatus the current status of the medicine stock.
     * @param newStatus the status of the stock.
     */
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
}
