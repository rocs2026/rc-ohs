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


    /**
     * Returns the unique ID of this student-section enrollment record.
     *
     * @return the enrollment record ID
     */
    public Long getId() {
        return id;
    }


    /**
     * Updates the unique ID of this enrollment record.
     *
     * @param newId the new enrollment record ID
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Returns the student enrolled in the section.
     *
     * @return the enrolled student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Enrolls the given student in the section.
     *
     * @param newStudent the student to enroll
     */
    public void setStudent(Student newStudent) {
        this.student = newStudent;
    }

    /**
     * Returns the section the student is enrolled in.
     *
     * @return the enrolled section
     */
    public Section getSection() {
        return section;
    }

    /**
     * Places the student in the given section.
     *
     * @param newSection the section to place the student in
     */
    public void setSection(Section newSection) {
        this.section = newSection;
    }

    /**
     * Returns the date and time the student was enrolled in the section.
     *
     * @return the enrollment date and time
     */
    public LocalDateTime getEnrolledDate() {
        return enrolledDate;
    }

    /**
     * Records the date and time the student was enrolled in the section.
     *
     * @param newEnrolledDate the enrollment date and time
     */
    public void setEnrolledDate(LocalDateTime newEnrolledDate) {
        this.enrolledDate = newEnrolledDate;
    }
}