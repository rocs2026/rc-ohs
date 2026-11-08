package com.rocs.rc.ohs.app.model.student;


import com.rocs.rc.ohs.app.model.person.Person;

/** Represents a student in the system.*/
public class Student {

    /** The unique ID of the student. */
    private Long id;

    /** The student number. */
    private String studentNumber;

    /** The person associated with the student. */
    private Person person;


    /**
     * Returns the unique ID of this student record.
     *
     * @return the student ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Updates the unique ID of this student record.
     *
     * @param newId the new student ID
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Returns the person details linked to this student.
     *
     * @return the linked person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Links this student to the given person details.
     *
     * @param newPerson the person to link
     */
    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    /**
     * Returns the student number issued by the school.
     *
     * @return the student number
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * Changes the student number issued by the school.
     *
     * @param newStudentNumber the new student number
     */
    public void setStudentNumber(String newStudentNumber) {
        this.studentNumber = newStudentNumber;
    }
}
