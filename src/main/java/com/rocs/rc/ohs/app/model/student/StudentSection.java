package com.rocs.rc.ohs.app.model.student;

import com.rocs.rc.ohs.app.model.section.Section;

import java.time.LocalDateTime;

/** Represents a student's enrollment in a section.*/
public class  StudentSection {

    /** The unique ID of the student section record. */
    private Long id;

    /** The student assigned to the section. */
    private Student student;

    /** The section assigned to the student. */
    private Section section;

    /** The date the student enrolled. */
    private LocalDateTime enrolledDate;


    /** @return the enrollment record ID */
    public Long getId() {
        return id;
    }


    /** @param newId the new enrollment record ID */
    public void setId(Long newId) {
        this.id = newId;
    }

    /** @return the enrolled student */
    public Student getStudent() {
        return student;
    }

    /** @param newStudent the student to enroll */
    public void setStudent(Student newStudent) {
        this.student = newStudent;
    }

    /** @return the enrolled section */
    public Section getSection() {
        return section;
    }

    /** @param newSection the section to place the student in */
    public void setSection(Section newSection) {
        this.section = newSection;
    }

    /** @return the enrollment date and time */
    public LocalDateTime getEnrolledDate() {
        return enrolledDate;
    }

    /** @param newEnrolledDate the enrollment date and time */
    public void setEnrolledDate(LocalDateTime newEnrolledDate) {
        this.enrolledDate = newEnrolledDate;
    }
}
