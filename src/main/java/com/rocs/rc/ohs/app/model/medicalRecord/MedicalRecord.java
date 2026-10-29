package com.rocs.rc.ohs.app.model.medicalRecord;

public class MedicalRecord {
    private String studentId;
    private Integer medicalConditions;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(Integer medicalConditions) {
        this.medicalConditions = medicalConditions;
    }
}