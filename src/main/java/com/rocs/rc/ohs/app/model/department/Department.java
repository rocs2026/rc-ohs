package com.rocs.rc.ohs.app.model.department;

/** Represents a department in the system. */
public class Department {

    /** The unique ID of the department. */
    private Long id;

    /** The name of the department. */
    private String departmentName;

    /** @return the department ID */
    public Long getDepartmentId() {
        return id;
    }

    /** @param newDepartmentId the new department ID */
    public void setDepartmentId(Long newDepartmentId) {
        this.id = newDepartmentId;
    }

    /** @return the department name */
    public String getDepartmentName() {
        return departmentName;
    }

    /** @param newDepartmentName the new department name */
    public void setDepartmentName(String newDepartmentName) {
        this.departmentName = newDepartmentName;
    }
}
