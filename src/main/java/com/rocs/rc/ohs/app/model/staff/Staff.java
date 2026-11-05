
package com.rocs.rc.ohs.app.model.staff;

import com.rocs.rc.ohs.app.model.person.Person;

/**
 * Represents a staff member in the system.
 */
public class Staff {

    /** The unique ID of the staff record. */
    private Integer staffId;

    /** The person associated with the staff member. */
    private Person person;

    /** The staff number. */
    private String staffNumber;

    /** The position of the staff member. */
    private String position;

    /** The department of the staff member. */
    private String department;

    /**
     * Retrieves the staff ID.
     *
     * @return The staff ID.
     */
    public Integer getStaffId() {
        return staffId;
    }

    /**
     * Sets the staff ID.
     *
     * @param newStaffId The staff ID.
     */
    public void setStaffId(Integer newStaffId) {
        this.staffId = newStaffId;
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
     * Retrieves the staff number.
     *
     * @return The staff number.
     */
    public String getStaffNumber() {
        return staffNumber;
    }

    /**
     * Sets the staff number.
     *
     * @param newStaffNumber The staff number.
     */
    public void setStaffNumber(String newStaffNumber) {
        this.staffNumber = newStaffNumber;
    }

    /**
     * Retrieves the position.
     *
     * @return The position.
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the position.
     *
     * @param newPosition The position.
     */
    public void setPosition(String newPosition) {
        this.position = newPosition;
    }

    /**
     * Retrieves the department.
     *
     * @return The department.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department.
     *
     * @param newDepartment The department.
     */
    public void setDepartment(String newDepartment) {
        this.department = newDepartment;
    }
}
