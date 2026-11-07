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


    /** @return the position ID */
    public Long getPositionId() {
        return id;
    }

    /** @param newPositionId the new position ID */
    public void setPositionId(Long newPositionId) {
        this.id = newPositionId;
    }

    /** @return the position title */
    public String getPositionName() {
        return positionName;
    }

    /** @param newPositionName the new position title */
    public void setPositionName(String newPositionName) {
        this.positionName = newPositionName;
    }

    /** @return the position category */
    public String getCategory() {
        return category;
    }

    /** @param newCategory the category to group this position under */
    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    /** @return the owning department */
    public Department getDepartment() {
        return department;
    }

    /** @param newDepartment the department to assign */
    public void setDepartment(Department newDepartment) {
        this.department = newDepartment;
    }
}
