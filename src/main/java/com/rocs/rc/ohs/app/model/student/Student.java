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


    public Long getId() {
        return id;
    }

    /** Sets the unique ID of this student record. */
    public void setId(Long newId) {
        this.id = newId;
    }

    /** Returns the person details linked to this student. */
    public Person getPerson() {
        return person;
    }

    /** Sets the person details linked to this student. */
    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    /** Returns the student number issued by the school. */
    public String getStudentNumber() {
        return studentNumber;
    }

    /** Sets the student number issued by the school. */
    public void setStudentNumber(String newStudentNumber) {
        this.studentNumber = newStudentNumber;
    }
}
