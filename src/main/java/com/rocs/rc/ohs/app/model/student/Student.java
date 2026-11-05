
package com.rocs.rc.ohs.app.model.student;

import com.rocs.rc.ohs.app.model.person.Person;

/**
 * Represents a student in the system.
 */
public class Student {

    /** The unique ID of the student. */
    private Long id;

    /** The person associated with the student. */
    private Person person;

    /** The student number. */
    private String studentNumber;

    /**
     * Retrieves the student ID.
     *
     * @return The student ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the student ID.
     *
     * @param newId The student ID.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Retrieves the person.
     *
     * @return The person.
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets the person.
     *
     * @param newPerson The person.
     */
    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    /**
     * Retrieves the student number.
     *
     * @return The student number.
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * Sets the student number.
     *
     * @param newStudentNumber The student number.
     */
    public void setStudentNumber(String newStudentNumber) {
        this.studentNumber = newStudentNumber;
    }
}
