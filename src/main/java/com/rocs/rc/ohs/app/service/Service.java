package com.rocs.rc.ohs.app.service;

import com.rocs.rc.ohs.app.model.medicalRecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.search.Search;
import com.rocs.rc.ohs.app.model.staff.Staff;
import com.rocs.rc.ohs.app.model.student.Student;
import java.util.HashMap;
import java.util.Map;

public class Service {
    private final Map<String, Student> students = new HashMap<>();
    private final Map<String, MedicalRecord> records = new HashMap<>();

    public Service() {

        Student s = new Student();
        s.setStudentId("2024-0001");
        s.setStudentNumber("S-001");
        s.setAcademicLevel("Senior High");
        s.setYearLevel("Grade 11");
        s.setSectionId(1);
        students.put(s.getStudentId(), s);

        MedicalRecord r = new MedicalRecord();
        r.setStudentId("2024-0001");
        r.setMedicalConditions(2);
        records.put(r.getStudentId(), r);
    }

    public MedicalRecord searchPatient(Staff staff, Search search) {

        if (staff == null || staff.getDepartment() == null
                || !staff.getDepartment().equalsIgnoreCase("Clinic")) {
            throw new SecurityException("Only clinic staff can view medical records");
        }

        if (search == null || search.getStudentId() == null
                || search.getStudentId().isBlank()) {
            throw new IllegalArgumentException("Student ID is required");
        }
        String studentId = search.getStudentId().trim();

        if (!students.containsKey(studentId)) {
            throw new IllegalStateException("No student found with ID " + studentId);
        }

        MedicalRecord record = records.get(studentId);
        if (record == null) {
            throw new IllegalStateException("Student " + studentId + " has no medical record");
        }
        return record;
    }
}