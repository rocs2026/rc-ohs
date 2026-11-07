package com.rocs.rc.ohs.app.model.department;

/** Represents a department in the system. */
public class Department {

    /** The unique ID of the department. */
    private Long id;

    /** The name of the department. */
    private String departmentName;


    public Long getDepartmentId() {
        return id;
    }

    public void setDepartmentId(Long newDepartmentId) {
        this.id = newDepartmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String newDepartmentName) {
        this.departmentName = newDepartmentName;
    }
}
