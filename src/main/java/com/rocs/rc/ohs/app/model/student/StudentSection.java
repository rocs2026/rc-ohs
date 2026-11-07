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


    public Long getId() {
        return id;
    }


    public void setId(Long newId) {
        this.id = newId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student newStudent) {
        this.student = newStudent;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section newSection) {
        this.section = newSection;
    }

    public LocalDateTime getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDateTime newEnrolledDate) {
        this.enrolledDate = newEnrolledDate;
    }
}
