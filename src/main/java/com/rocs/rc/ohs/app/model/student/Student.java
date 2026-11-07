package com.rocs.rc.ohs.app.model.student;


import com.rocs.rc.ohs.app.model.person.Person;

/** Represents a student in the system.*/
public class Student {

    /** The unique ID of the student. */
    private Long id;

    /** The person associated with the student. */
    private Person person;

    /** The student number. */
    private String studentNumber;


    /** @return the student ID */
    public Long getId() {
        return id;
    }

    /** @param newId the new student ID */
    public void setId(Long newId) {
        this.id = newId;
    }

    /** @return the linked person */
    public Person getPerson() {
        return person;
    }

    /** @param newPerson the person to link */
    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    /** @return the student number */
    public String getStudentNumber() {
        return studentNumber;
    }

    /** @param newStudentNumber the new student number */
    public void setStudentNumber(String newStudentNumber) {
        this.studentNumber = newStudentNumber;
    }
}
