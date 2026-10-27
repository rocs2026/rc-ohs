package com.rocs.rc.ohs.app.model.consultation;

import java.sql.Timestamp;
public class Consultation {

    private int consultationId;
    private int medicalRecordId;
    private int staffId;
    private String recordType;
    private Timestamp consultationDate;
    private String complaint;
    private String treatment;
    private String actionTaken;
    private String remarks;

    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Timestamp getConsultationDate() {
        return consultationDate;
    }

    public void setConsultationDate(Timestamp consultationDate) {
        this.consultationDate = consultationDate;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
