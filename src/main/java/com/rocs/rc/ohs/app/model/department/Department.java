
package com.rocs.rc.ohs.app.model.department;

/**
 * Represents a department in the system.
 */
public class Department {

    /** The unique ID of the department. */
    private Long departmentId;

    /** The name of the department. */
    private String departmentName;

    /**
     * Retrieves the ID of the department.
     *
     * @return The ID of the department.
     */
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * Sets the ID of the department.
     *
     * @param newDepartmentId The ID of the department.
     */
    public void setDepartmentId(Long newDepartmentId) {
        this.departmentId = newDepartmentId;
    }

    /**
     * Retrieves the name of the department.
     *
     * @return The name of the department.
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * Sets the name of the department.
     *
     * @param newDepartmentName The name of the department.
     */
    public void setDepartmentName(String newDepartmentName) {
        this.departmentName = newDepartmentName;
    }
}
