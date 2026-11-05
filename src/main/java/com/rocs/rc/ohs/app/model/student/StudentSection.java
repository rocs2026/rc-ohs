
package com.rocs.rc.ohs.app.model.student;

import com.rocs.rc.ohs.app.model.section.Section;
import com.rocs.rc.ohs.app.model.student.Student;
import java.util.Date;

/**
 * Represents a student's enrollment in a section.
 */
public class StudentSection {

    /** The unique ID of the student section record. */
    private Long id;

    /** The student assigned to the section. */
    private Student student;

    /** The section assigned to the student. */
    private Section section;

    /** The date the student enrolled. */
    private Date enrolledDate;

    /**
     * Retrieves the student section ID.
     *
     * @return The student section ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the student section ID.
     *
     * @param newId The student section ID.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Retrieves the student.
     *
     * @return The student.
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Sets the student.
     *
     * @param newStudent The student.
     */
    public void setStudent(Student newStudent) {
        this.student = newStudent;
    }

    /**
     * Retrieves the section.
     *
     * @return The section.
     */
    public Section getSection() {
        return section;
    }

    /**
     * Sets the section.
     *
     * @param newSection The section.
     */
    public void setSection(Section newSection) {
        this.section = newSection;
    }

    /**
     * Retrieves the enrollment date.
     *
     * @return The enrollment date.
     */
    public Date getEnrolledDate() {
        return enrolledDate;
    }

    /**
     * Sets the enrollment date.
     *
     * @param newEnrolledDate The enrollment date.
     */
    public void setEnrolledDate(Date newEnrolledDate) {
        this.enrolledDate = newEnrolledDate;
    }
}
