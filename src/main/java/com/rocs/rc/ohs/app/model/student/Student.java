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

    public void setId(Long newId) {
        this.id = newId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String newStudentNumber) {
        this.studentNumber = newStudentNumber;
    }
}
