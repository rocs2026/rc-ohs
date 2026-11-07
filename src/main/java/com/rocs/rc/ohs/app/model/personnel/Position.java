package com.rocs.rc.ohs.app.model.personnel;

import com.rocs.rc.ohs.app.model.department.Department;

/** Represents a personnel position in the system. */
public class Position {

    /** The unique ID of the position. */
    private Long id;

    /** The name of the position. */
    private String positionName;

    /** The category of the position. */
    private String category;

    /** The department associated with the position. */
    private Department department;


    public Long getPositionId() {
        return id;
    }

    public void setPositionId(Long newPositionId) {
        this.id = newPositionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String newPositionName) {
        this.positionName = newPositionName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department newDepartment) {
        this.department = newDepartment;
    }
}
