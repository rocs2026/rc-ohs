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


    /** Returns the unique ID assigned to this position. */
    public Long getPositionId() {
        return id;
    }

    /** Sets the unique ID of this position. */
    public void setPositionId(Long newPositionId) {
        this.id = newPositionId;
    }

    /** Returns the title of this position, such as the job title shown in OHS records. */
    public String getPositionName() {
        return positionName;
    }

    /** Sets the title of this position. */
    public void setPositionName(String newPositionName) {
        this.positionName = newPositionName;
    }

    /** Returns the category this position is grouped under. */
    public String getCategory() {
        return category;
    }

    /** Sets the category this position is grouped under. */
    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    /** Returns the department this position belongs to. */
    public Department getDepartment() {
        return department;
    }

    /** Sets the department this position belongs to. */
    public void setDepartment(Department newDepartment) {
        this.department = newDepartment;
    }
}
