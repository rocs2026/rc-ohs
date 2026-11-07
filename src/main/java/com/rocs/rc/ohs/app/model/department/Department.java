package com.rocs.rc.ohs.app.model.department;

/** Represents a department in the system. */
public class Department {

    /** The unique ID of the department. */
    private Long id;

    /** The name of the department. */
    private String departmentName;

    /** Returns the unique ID assigned to this department. */
    public Long getDepartmentId() {
        return id;
    }

    /** Sets the unique ID of this department. */
    public void setDepartmentId(Long newDepartmentId) {
        this.id = newDepartmentId;
    }

    /** Returns the name of this department as shown in OHS records. */
    public String getDepartmentName() {
        return departmentName;
    }

    /** Updates the department name that appears in OHS records. */
    public void setDepartmentName(String newDepartmentName) {
        this.departmentName = newDepartmentName;
    }
}
