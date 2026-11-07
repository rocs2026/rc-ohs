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


    /** Returns the unique ID of this student-section enrollment record. */
    public Long getId() {
        return id;
    }


    /** Sets the unique ID of this student-section enrollment record. */
    public void setId(Long newId) {
        this.id = newId;
    }

    /** Returns the student enrolled in the section. */
    public Student getStudent() {
        return student;
    }

    /** Sets the student enrolled in the section. */
    public void setStudent(Student newStudent) {
        this.student = newStudent;
    }

    /** Returns the section the student is enrolled in. */
    public Section getSection() {
        return section;
    }

    /** Sets the section the student is enrolled in. */
    public void setSection(Section newSection) {
        this.section = newSection;
    }

    /** Returns the date and time the student was enrolled in the section. */
    public LocalDateTime getEnrolledDate() {
        return enrolledDate;
    }

    /** Sets the date and time the student was enrolled in the section. */
    public void setEnrolledDate(LocalDateTime newEnrolledDate) {
        this.enrolledDate = newEnrolledDate;
    }
}
